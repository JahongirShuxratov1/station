package com.example.stations.util;

import com.example.stations.entity.Code;
import com.example.stations.entity.User;
import com.example.stations.repository.CodeRepository;
import com.example.stations.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class Utils {
    private final JavaMailSender mailSender;
    private final CodeRepository codeRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final double EARTH_RADIUS_KM = 6371.0;

    public Utils(JavaMailSender mailSender, CodeRepository codeRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.mailSender = mailSender;
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public boolean checkIfWithin10Km(String lat1Str, String lon1Str, String lat2Str, String lon2Str) {
        double lat1 = Double.parseDouble(lat1Str);
        double lon1 = Double.parseDouble(lon1Str);
        double lat2 = Double.parseDouble(lat2Str);
        double lon2 = Double.parseDouble(lon2Str);

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        return distance <= 10;
    }

    public boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty())
            return phoneNumber.matches("^[0-9]{9}$");
        else return false;
    }

    public boolean checkEmail(String email) {
        if (email == null || email.isEmpty())
            return false;
        return email.contains("@");
    }

    public String getSecretKey() {
        return jwtUtil.getSecretKeyAsString();
    }

    public String getCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000); // Generates a number between 10000 and 99999
        return String.valueOf(randomCode);
    }

    public boolean checkCode(String email, String code, Long deviceId) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Code> optional = this.codeRepository.findByUserIdAndDeviceId(user.getId(), deviceId);
            if (optional.isPresent()) {
                Code c = optional.get();
                return code != null && !code.trim().isEmpty() && c.getCode().equals(code);
            }
        }
        return false;
    }

    public boolean sendCodeToMail(String mail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("xalqaroshartnomalaruz@gmail.com");
            helper.setTo(mail);
            helper.setSubject("🔐 Logistics APP – Tasdiqlash kodi");

            String htmlContent = """
                        <html>
                            <body style="margin: 0; padding: 0; background: #f4f4f4; font-family: Arial, sans-serif;">
                                <div style="max-width: 600px; margin: 40px auto; padding: 30px; background: white; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
                                    <h2 style="text-align: center; color: #333;">Tasdiqlash uchun kod</h2>
                                    <p style="text-align: center; color: #666;">Quyidagi kodni ilovada tasdiqlash uchun kiriting:</p>
                                    <div style="margin: 30px auto; width: fit-content; background: linear-gradient(to right, #4facfe, #00f2fe); padding: 20px 40px; border-radius: 12px; box-shadow: 0 0 10px rgba(0,0,0,0.15);">
                                        <span style="font-size: 36px; font-weight: bold; color: white; letter-spacing: 5px;">%s</span>
                                    </div>
                                    <p style="text-align: center; color: #999; font-size: 14px;">Agar bu siz bo‘lmasangiz, bu xabarni e’tiborsiz qoldiring.</p>
                                </div>
                            </body>
                        </html>
                    """.formatted(code);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean existUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }


}

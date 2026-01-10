package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InternalServerException;
import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.exception.ServiceUnavailableException;
import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.OtpSendRequest;
import com.dekhokaun.mindarobackend.payload.request.OtpVerifyRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OtpService {
    private static final SecureRandom random = new SecureRandom();

    private final OtpRepository otpRepository;

    private final UserRepository userRepository;

    public OtpResponse generateOtp(OtpSendRequest request) {
        int otp = 100000 + random.nextInt(900000);
        boolean isMobile = request.getMobileOrEmail() != null &&
                "mobile".equalsIgnoreCase(request.getOtpType());
        String otpMessage = String.format("Your OTP is %s. It is valid for a 10 minutes only.", otp);

        UUID userId = UUID.fromString(request.getUserid());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found with ID: " + request.getUserid()));

        otpRepository.deleteByUseridAndMobileAndOtpType(userId, request.getMobileOrEmail(), request.getOtpType());

        Otp otpEntity = new Otp();
        otpEntity.setUserid(userId);
        otpEntity.setOtpType(request.getOtpType());
        otpEntity.setCountryCodeMobile(request.getCountryCode());
        otpEntity.setOtp(String.valueOf(otp));
        otpEntity.setSmsCounter("0");
        otpEntity.setSmsResponse(otpMessage);
        otpEntity.setSmsapi("example_api");
        otpEntity.setStatus("PENDING");

        if (request.getIp() != null && !request.getIp().isBlank()) {
            otpEntity.setIp(request.getIp());
        }

        try {
            if (isMobile) {
                otpEntity.setMobile(request.getMobileOrEmail());
                sendSmsRequest(request.getMobileOrEmail(), user.getName(), String.valueOf(otp));
            } else {
                sendEmailRequest(request.getMobileOrEmail(), String.valueOf(otp));
            }
        } catch (IOException e) {
            throw new InternalServerException(
                    String.format("Unable to send OTP on %s, Please try again later", request.getMobileOrEmail())
            );
        }

        otpRepository.save(otpEntity);
        return new OtpResponse(otpMessage);
    }


    public OtpResponse verifyOtp(OtpVerifyRequest request) {
        if (request.getOtp() == null || request.getOtp().isEmpty()) {
            return new OtpResponse("OTP is required!");
        }

        Optional<Otp> storedOtp = otpRepository.findByUseridAndMobileAndOtpTypeAndStatus(
                UUID.fromString(request.getUserid()), request.getMobileOrEmail(), request.getOtpType(), "PENDING"
        );

        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
            // otpRepository.deleteByUseridAndMobileAndOtpType(request.getUserid(), request.getMobileOrEmail(), request.getOtpType());
            storedOtp.get().setStatus("VERIFIED");
            otpRepository.save(storedOtp.get());
            return new OtpResponse("OTP verified successfully!");
        }

        return new OtpResponse("OTP verification failed!");
    }


    private void sendSmsRequest(String mobileNumber, String username, String otp) throws IOException {
        String apiUrl = "https://we8.in/smsapi/smsapi.php";

        String data1Json = String.format("""
            {
                "usercode":"advijr",
                "smsuser":"%s",
                "smskey":"Ymjsd5h7@3",
                "mobiles":"%s",
                "message":"OTP for login is %s, valid for 10 minutes. -Serviced by Mindaro",
                "senderid":"MINDRO",
                "entityid":"1501544040000010555",
                "tempid":"1107172551760125165",
                "v1":"stage_of_sending_sms",
                "v2":"user_id_reference",
                "v3":"any_value",
                "v4":"any_value",
                "v5":"datetimesent"
            }
        """, username, mobileNumber, otp);

        // URL encode each part
        String encodedApi1 = "api1=" + URLEncoder.encode("otp", StandardCharsets.UTF_8);
        String encodedData1 = "data1=" + URLEncoder.encode(data1Json, StandardCharsets.UTF_8);
        String encodedChksum1 = "chksum1=" + URLEncoder.encode("chksum", StandardCharsets.UTF_8);

        String formParams = String.join("&", encodedApi1, encodedData1, encodedChksum1);

        int responseCode = getResponseCode(apiUrl, formParams);
        System.out.println("Response Code: " + responseCode);
    }

    private static int getResponseCode(String apiUrl, String formParams) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = formParams.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return conn.getResponseCode();
    }

    private void sendEmailRequest(String email, String otp) {
        throw new ServiceUnavailableException("Email OTP service is temporarily unavailable", "Please try using mobile OTP or contact support");
    }
}

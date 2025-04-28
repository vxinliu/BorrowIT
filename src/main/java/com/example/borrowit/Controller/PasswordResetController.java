package com.example.borrowit.Controller;

import com.example.borrowit.service.impl.EmailService;
import com.example.borrowit.service.impl.PasswordResetTokenService;
import com.example.borrowit.service.impl.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService tokenService;
    private final EmailService emailService;

    public PasswordResetController(UserService userService,
                                   PasswordResetTokenService tokenService,
                                   EmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    /**
     * Endpoint pour demander une réinitialisation de mot de passe
     */
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        try {
            // Vérifie si l'email existe
            if (userService.getUserByEmail(email).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("AUCUN_COMPTE", "Aucun compte associé à cet email"));
            }

            // Génère et envoie le token
            String token = tokenService.generateToken(email);
            emailService.sendPasswordResetEmail(email, token);

            return ResponseEntity.ok()
                    .body(new SuccessResponse("EMAIL_ENVOYE", "Email de réinitialisation envoyé"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("ERREUR_SERVEUR", "Erreur lors du traitement de la demande"));
        }
    }

    /**
     * Endpoint pour confirmer la réinitialisation du mot de passe
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest request) {
        try {
            // Valide le token et récupère l'email
            String email = tokenService.validateToken(request.getToken());

            // Met à jour le mot de passe
            userService.updatePassword(email, request.getNewPassword());

            return ResponseEntity.ok()
                    .body(new SuccessResponse("MDP_REINITIALISE", "Mot de passe réinitialisé avec succès"));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("LIEN_EXPIRE", "Le lien de réinitialisation a expiré"));
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("TOKEN_INVALIDE", "Token de réinitialisation invalide"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ERREUR_TRAITEMENT", e.getMessage()));
        }
    }

    // === Classes internes pour la structure des réponses ===

    public static class ResetRequest {
        private String token;
        private String newPassword;

        // Getters et Setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class SuccessResponse {
        private String code;
        private String message;

        public SuccessResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        // Getters
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    public static class ErrorResponse {
        private String errorCode;
        private String errorMessage;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        // Getters
        public String getErrorCode() { return errorCode; }
        public String getErrorMessage() { return errorMessage; }
    }
}

package com.innervix.model3d.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String uploadDir = "uploads";
    private Security security = new Security();

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public static class Security {
        private boolean enabled = false;
        private String jwtSecret;
        private long tokenValiditySeconds = 86400;
        private BootstrapAdmin bootstrapAdmin = new BootstrapAdmin();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public long getTokenValiditySeconds() {
            return tokenValiditySeconds;
        }

        public void setTokenValiditySeconds(long tokenValiditySeconds) {
            this.tokenValiditySeconds = tokenValiditySeconds;
        }

        public BootstrapAdmin getBootstrapAdmin() {
            return bootstrapAdmin;
        }

        public void setBootstrapAdmin(BootstrapAdmin bootstrapAdmin) {
            this.bootstrapAdmin = bootstrapAdmin;
        }
    }

    public static class BootstrapAdmin {
        private String username = "admin";
        private String password = "admin";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

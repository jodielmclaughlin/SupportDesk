package io.nology.supportdesk.config.factory.user;

import io.nology.supportdesk.user.entity.UserRole;

public class UserFactoryOptions {
    String name;
    String email;
    String passwordHash;
    UserRole role;

    private UserFactoryOptions(Builder builder) {
        this.email = builder.email;
        this.name = builder.name;
        this.passwordHash = builder.passwordHash;
        this.role = builder.role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String email;
        private String passwordHash;
        private String name;
        private UserRole role;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserFactoryOptions build() {
            return new UserFactoryOptions(this);
        }
    }
}

package com.Springboot.Security_client.entity;

import java.util.Calendar;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
		
		private static final int EXP_TIME = 10;
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String token;
		private Date expirationTime;
		
		@OneToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "user_id",
					nullable = false,
					foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
		private User user;
		
		public PasswordResetToken(User user, String token) {
			super();
			this.user = user;
			this.token = token;
			this.expirationTime = calculateExpTime(EXP_TIME);
		}

		public PasswordResetToken(String token ) {
			super();
			this.token = token;
			this.expirationTime = calculateExpTime(EXP_TIME);
		}
		
		private Date calculateExpTime(int expTime) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(new Date().getTime());
			calendar.add(Calendar.MINUTE, expTime);
			return new Date(calendar.getTime().getTime());
		}
}
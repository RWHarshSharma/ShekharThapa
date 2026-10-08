package hack2skill.draft;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

// "app_users" because "user" is a reserved word in PostgreSQL.
@Entity
@Table(name = "app_users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	// Stores a password hash only, never the plain password.
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt = Instant.now();

	protected AppUser() {
	}

	public AppUser(String email, String passwordHash) {
		this.email = email;
		this.passwordHash = passwordHash;
	}

	public Long getId() { return id; }
	public String getEmail() { return email; }
	public String getPasswordHash() { return passwordHash; }
	public Instant getCreatedAt() { return createdAt; }
}

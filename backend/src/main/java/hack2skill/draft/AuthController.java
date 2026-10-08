package hack2skill.draft;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

	private final AppUserRepository users;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public AuthController(AppUserRepository users) {
		this.users = users;
	}

	public record Credentials(String email, String password) {}

	@PostMapping("/api/auth/register")
	public Map<String, String> register(@RequestBody Credentials in) {
		String email = normalizeEmail(in.email());
		validate(email, in.password());
		if (users.findByEmail(email).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists.");
		}
		users.save(new AppUser(email, encoder.encode(in.password())));
		return Map.of("email", email);
	}

	@PostMapping("/api/auth/login")
	public Map<String, String> login(@RequestBody Credentials in) {
		String email = normalizeEmail(in.email());
		validate(email, in.password());
		AppUser user = users.findByEmail(email)
				.filter(u -> encoder.matches(in.password(), u.getPasswordHash()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password."));
		return Map.of("email", user.getEmail());
	}

	private static String normalizeEmail(String email) {
		return email == null ? "" : email.trim().toLowerCase();
	}

	private static void validate(String email, String password) {
		if (!email.contains("@") || email.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a valid email.");
		}
		if (password == null || password.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters.");
		}
	}
}

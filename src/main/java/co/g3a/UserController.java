package co.g3a;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.Map;

@RestController
public class UserController {
	@GetMapping("employee")
	@ResponseBody
	@PreAuthorize("hasAuthority('APPROLE_Admin')")
	//@PreAuthorize("hasAuthority('APPROLE_user')")
	public String Admin() {
		return "Employee Details";
	}


	@GetMapping("/user")
	@PreAuthorize("hasAuthority('APPROLE_readuser')")
	public String getUser(){
		return "Access Granted : Read User Details";
	}

    @PostMapping ("/user")
	@PreAuthorize("hasAuthority('APPROLE_updateuser')")
	public String updateUser(){
		return "Access Granted : Update User details";
	}


	@GetMapping(path = "/autorities")
	public String getAuthorities(@AuthenticationPrincipal Jwt jwt) {
		Collection<String> roles = jwt.getClaimAsStringList("roles");
		return "Roles: " + roles;
	}

	@GetMapping("/claims")
	public Map<String, Object> claims(@AuthenticationPrincipal Jwt jwt) {
		return jwt.getClaims();
	}
}
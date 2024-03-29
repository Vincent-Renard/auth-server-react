package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.dtos.in.BanDomainRequest;
import com.example.auth.server.authentification.facade.dtos.in.BanDomainsRequest;
import com.example.auth.server.authentification.facade.dtos.in.BanUserRequest;
import com.example.auth.server.authentification.facade.dtos.in.UpdateRolesRequest;
import com.example.auth.server.authentification.facade.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.authentification.facade.dtos.out.User;
import com.example.auth.server.authentification.facade.exceptions.NotSuchAdminException;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.exceptions.UserAlreadyBanException;
import com.example.auth.server.authentification.facade.services.admin.AdminService;
import com.example.auth.server.authentification.facade.services.server.DomainService;
import com.example.auth.server.authentification.facade.services.server.ServerService;
import com.example.auth.server.authentification.facade.services.users.roles.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@RestController
@RequestMapping("/auth/admin")
@AllArgsConstructor
public class AdminController {

	private AdminService adminService;
	private DomainService domainService;
	private ServerService serverService;
	private RoleService roleService;


	@PostMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> addDomain(Principal admin, @RequestBody BanDomainRequest domain) {
		domainService.addForbidenDomain(Long.parseLong(admin.getName()), domain.getDomain());
		return ResponseEntity.ok().build();

	}

	@PostMapping(value = "/domains/list", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> addDomains(Principal admin, @RequestBody BanDomainsRequest request) {

		domainService.addForbidenDomains(Long.parseLong(admin.getName()), request.getDomains());
		return ResponseEntity.ok().build();

	}


	@DeleteMapping(value = "/domains/{dom}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> delDomain(Principal admin, @PathVariable(name = "dom") String domain) {
		domainService.delForbidenDomain(Long.parseLong(admin.getName()), domain);
		return ResponseEntity.noContent().build();

	}

	@GetMapping(value = "/state", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthServerStateAdmin> stateAdmin(Principal admin) throws NotSuchUserException {
		return ResponseEntity.ok(adminService.getServerStateAdmin(Long.parseLong(admin.getName())));
	}

	@GetMapping(value = "/users/{iduser}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> showUser(Principal admin, @PathVariable(name = "iduser") long idUser) throws NotSuchUserException {
		return ResponseEntity.ok(User.from(adminService.showUser(Long.parseLong(admin.getName()), idUser)));
	}

	@GetMapping(value = "/users/", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Collection<User>> searchUsers(Principal admin, @RequestParam(name = "maildomain", required = false) String domain
			, @RequestParam(required = false, name = "role") String role) {


		HashSet<User> e = new HashSet<>();


		if (domain != null) {
			HashSet<User> r = adminService.getAllUsersWithDomain(domain)
					.stream()
					.map(User::from)
					.collect(Collectors.toCollection(HashSet::new));
			if (e.isEmpty())
				e.addAll(r);
			else
				e.retainAll(r);


		}
		if (role != null) {
			HashSet<User> r = roleService.getAllUsersWithRole(role)
					.stream()
					.map(User::from)
					.collect(Collectors.toCollection(HashSet::new));

			if (e.isEmpty())
				e.addAll(r);
			else
				e.retainAll(r);


		}
		return ResponseEntity.ok(e);
	}

	@PatchMapping(value = "/users/{id}/roles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> updateRoles(Principal admin, @PathVariable(value = "id") long idUser, @RequestBody UpdateRolesRequest updateRolesRequest) throws NotSuchUserException, NotSuchAdminException {
		return ResponseEntity
				.ok(User.from(roleService.updateRoles(idUser,
						updateRolesRequest.getRoles(),
						Long.parseLong(admin.getName()))));

	}

	@PostMapping(value = "/users/{iduser}/ban", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> banUser(Principal principal, @PathVariable(name = "iduser") long idUser, @RequestBody BanUserRequest bur) throws NotSuchUserException, UserAlreadyBanException {
		return ResponseEntity.ok(
				User.from(adminService.banUser(idUser,
						bur.getReason(),
						Long.parseLong(principal.getName()))));

	}

	@DeleteMapping(value = "/users/{iduser}/ban")
	public ResponseEntity<Void> unBanUser(Principal principal, @PathVariable(name = "iduser") long idUser) throws NotSuchUserException {
		adminService.unBanUser(idUser, Long.parseLong(principal.getName()));
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/clean")
	public ResponseEntity<Void> clear() {
		serverService.clear();
		return ResponseEntity.noContent().build();
	}


}

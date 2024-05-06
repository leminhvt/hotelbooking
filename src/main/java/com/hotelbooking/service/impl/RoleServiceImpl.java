package com.hotelbooking.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hotelbooking.exception.RoleException;
import com.hotelbooking.exception.UserException;
import com.hotelbooking.model.Role;
import com.hotelbooking.model.User;
import com.hotelbooking.repository.RoleRepository;
import com.hotelbooking.repository.UserRepository;
import com.hotelbooking.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role createRole(Role theRole) {
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		Role role = new Role(roleName);
		if (roleRepository.existsByName(roleName)) {
			throw new RoleException(theRole.getName() + " role already exists");
		}
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long id) {
		this.removeAllUsersFromRole(id);
		roleRepository.deleteById(id);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);

		if (user.isPresent() && role.isPresent() && role.get().getUsers().contains(user.get())) {
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}

		throw new UsernameNotFoundException("User not found with ID: " + userId);
	}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);

		if (user.isPresent() && user.get().getRoles().contains(role.get())) {
			throw new UserException(user.get().getFirstName() + " is already to the" + role.get().getName() + " role");
		}

		if (role.isPresent()) {
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
		}
		return user.get();

	}

	@Override
	public Role removeAllUsersFromRole(Long roleId) {
		Optional<Role> optional = roleRepository.findById(roleId);
		optional.get().removeAllUserFromRole();

		return roleRepository.save(optional.get());
	}

}

package ru.amalnev.jnms.common.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.amalnev.jnms.common.model.entities.security.Authority;
import ru.amalnev.jnms.common.model.entities.security.User;
import ru.amalnev.jnms.common.model.entities.security.UserRole;
import ru.amalnev.jnms.common.model.repositories.IAuthorityRepository;
import ru.amalnev.jnms.common.model.repositories.IUserRepository;
import ru.amalnev.jnms.common.model.repositories.IUserRoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Данный класс реализует бин с интерфейсом UserDetailsService, который необходим
 * для функционирования Spring security. Также реализованы методы для работы с сущностями
 * User и некоторые утилиты.
 *
 * @author Aleksei Malnev
 */
@Service
public class SecurityService implements UserDetailsService
{
    @Setter(onMethod = @__({@Autowired}))
    private IUserRepository userRepository;

    @Setter(onMethod = @__({@Autowired}))
    private IAuthorityRepository authorityRepository;

    @Setter(onMethod = @__({@Autowired}))
    private IUserRoleRepository roleRepository;

    @Setter(onMethod = @__({@Autowired}))
    private PasswordEncoder passwordEncoder;

    public List<UserRole> findAllUserRoles()
    {
        return (List<UserRole>) roleRepository.findAll();
    }

    public List<Authority> findAllAuthorities()
    {
        return (List<Authority>) authorityRepository.findAll();
    }

    public List<User> findAllUsers()
    {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> findUserById(final Long id)
    {
        return userRepository.findById(id);
    }

    public void saveUser(final User user, Boolean...hashPassword)
    {
        boolean enc = true;
        if(hashPassword.length != 0 && !hashPassword[0]) enc = false;
        if(enc)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public void deleteUserById(final Long userId)
    {
        userRepository.deleteById(userId);
    }

    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
    {
        final User user = userRepository.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    /**
     * Данная функция возвращает уровень привилегий текущего пользователя.
     *
     * @return
     */
    public int getCurrentPrivilegeLevel()
    {
        //Текущему пользователю может быть назначено несколько ролей.
        //Найдем роль с максимальными привилегиями.
        final int strongestPrivilege = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .mapToInt(auth -> roleRepository.findByName(auth.getAuthority()).getPrivilegeLevel())
                .max()
                .orElse(0);

        return strongestPrivilege;
    }

    public User getCurrentUser()
    {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public boolean isTechSupportOperator(final User user)
    {
        if(user == null) return false;
        if(user.getWorkGroup() == null) return false;
        return user.getWorkGroup().getName().equals("Support operators");
    }
}

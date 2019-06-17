package ru.amalnev.jnmscommon.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.amalnev.jnmscommon.entities.security.Authority;
import ru.amalnev.jnmscommon.entities.security.User;
import ru.amalnev.jnmscommon.entities.security.UserRole;
import ru.amalnev.jnmscommon.repositories.IAuthorityRepository;
import ru.amalnev.jnmscommon.repositories.IUserRepository;
import ru.amalnev.jnmscommon.repositories.IUserRoleRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class SecurityService implements UserDetailsService
{
    @Setter(onMethod=@__({@Autowired}))
    private IUserRepository userRepository;

    @Setter(onMethod=@__({@Autowired}))
    private IAuthorityRepository authorityRepository;

    @Setter(onMethod=@__({@Autowired}))
    private IUserRoleRepository roleRepository;

    @Setter(onMethod=@__({@Autowired}))
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init()
    {
        /*Проверяем, являются ли пустыми репозитории пользователей и назначенных им ролей*/
        final List<User> users = findAllUsers();
        final List<UserRole> roles = findAllUserRoles();
        final List<Authority> authorities = findAllAuthorities();

        //Если репозитории пустые - создаем начальных пользователей и назначаем им роли
        //для того чтобы инициализировать подсистему безопасности в работоспособное начальное состояние
        if(users.size() == 0 && roles.size() == 0 && authorities.size() == 0)
        {
            final User root = new User("root", passwordEncoder.encode("root"));
            final UserRole rootRole = new UserRole("ROLE_ROOT", 15);

            userRepository.save(root);
            roleRepository.save(rootRole);

            final Authority rootAuthority = new Authority(root, rootRole);

            authorityRepository.save(rootAuthority);
        }
    }

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

    public void saveUser(final User user)
    {
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

    public int getCurrentPrivilegeLevel()
    {
        final Function<GrantedAuthority, Integer> getPrivlegeByAuthority =
                (final GrantedAuthority auth) -> roleRepository.findByName(auth.getAuthority()).getPrivilegeLevel();

        Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();

        final GrantedAuthority strongestAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .max(Comparator.comparingInt(auth -> getPrivlegeByAuthority.apply((GrantedAuthority) auth)))
                .orElse(null);
        if(strongestAuthority != null) return getPrivlegeByAuthority.apply(strongestAuthority);
        return 0;
    }
}

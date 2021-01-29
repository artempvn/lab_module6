package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dao.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserDaoImpl implements UserDao {

  private final SessionFactory sessionFactory;

  public UserDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public UserDto create(UserDto dto) {
    User user = new User(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(user);
    return new UserDto(user);
  }

  @Override
  public Optional<UserDtoFull> read(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<User> user = Optional.ofNullable(session.get(User.class, id));
    return user.map(UserDtoFull::new);
  }

  @Override
  public List<UserDto> readAll() {
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    criteria.from(User.class);
    List<User> users = session.createQuery(criteria).list();
    return users.stream().map(UserDto::new).collect(Collectors.toList());
  }
}

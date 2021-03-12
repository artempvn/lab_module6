package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserWithOrdersDto read(long id) {
    Optional<UserWithOrdersDto> user = userDao.read(id);
    return user.orElseThrow(ResourceNotFoundException.notFoundWithUser(id));
  }

  @Override
  public PageData<UserDto> readAll(PaginationParameter parameter) {
    return userDao.readAll(parameter);
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    return userDao.takeMostWidelyTagFromUserWithHighestCostOrders();
  }
}

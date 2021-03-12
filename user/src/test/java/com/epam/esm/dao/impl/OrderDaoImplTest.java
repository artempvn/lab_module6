package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class OrderDaoImplTest {
  public static final int NOT_EXISTING_USER_ID = 9999999;
  public static final int NOT_EXISTING_ORDER_ID = 9999999;
  @Autowired OrderDao orderDao;
  @Autowired UserDao userDao;
  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @AfterEach
  void setDown() {
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void create() {
    UserDto user = givenUser();
    long id = userDao.create(user).getId();
    TagDto tag = givenTag();
    var tag1 = tagDao.create(tag);
    CertificateWithTagsDto certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    OrderWithCertificatesWithTagsForCreationDto order = givenOrder();
    order.setUserId(id);
    order.setCertificates(List.of(certificate1));

    OrderWithCertificatesWithTagsForCreationDto actualOrder = orderDao.create(order);

    assertNotNull(actualOrder.getId());
  }

  @Test
  void readAllByUser() {
    UserDto user = givenUser();
    long id = userDao.create(user).getId();
    TagDto tag = givenTag();
    var tag1 = tagDao.create(tag);
    CertificateWithTagsDto certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    OrderWithCertificatesWithTagsForCreationDto order = givenOrder();
    order.setUserId(id);
    order.setCertificates(List.of(certificate1));
    orderDao.create(order);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<OrderDto> actualPage = orderDao.readAllByUser(id, parameter);

    assertEquals(1, actualPage.getContent().size());
  }

  @Test
  void readAllByNotExistingUser() {

    assertThrows(
        ResourceValidationException.class,
        () -> orderDao.readAllByUser(NOT_EXISTING_USER_ID, new PaginationParameter()));
  }

  @Test
  void readExistingOrderByUser() {
    UserDto user = givenUser();
    long userId = userDao.create(user).getId();
    TagDto tag = givenTag();
    var tag1 = tagDao.create(tag);
    CertificateWithTagsDto certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    OrderWithCertificatesWithTagsForCreationDto order = givenOrder();
    order.setUserId(userId);
    order.setCertificates(List.of(certificate1));
    long orderId = orderDao.create(order).getId();

    Optional<OrderWithCertificatesDto> actualOrder = orderDao.readOrderByUser(userId, orderId);

    assertTrue(actualOrder.isPresent());
  }

  @Test
  void readNonExistingOrderByUser() {
    UserDto user = givenUser();
    long userId = userDao.create(user).getId();
    TagDto tag = givenTag();
    var tag1 = tagDao.create(tag);
    CertificateWithTagsDto certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    OrderWithCertificatesWithTagsForCreationDto order = givenOrder();
    order.setUserId(userId);
    order.setCertificates(List.of(certificate1));

    Optional<OrderWithCertificatesDto> actualOrder =
        orderDao.readOrderByUser(userId, NOT_EXISTING_ORDER_ID);

    assertTrue(actualOrder.isEmpty());
  }

  @Test
  void readOrderByNotExistingUser() {

    assertThrows(
        ResourceValidationException.class,
        () -> orderDao.readOrderByUser(NOT_EXISTING_USER_ID, NOT_EXISTING_ORDER_ID));
  }

  OrderWithCertificatesWithTagsForCreationDto givenOrder() {
    OrderWithCertificatesWithTagsForCreationDto order =
        new OrderWithCertificatesWithTagsForCreationDto();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    return order;
  }

  UserDto givenUser() {
    UserDto user = new UserDto();
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  CertificateWithTagsDto givenCertificate() {
    CertificateWithTagsDto certificate = new CertificateWithTagsDto();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}

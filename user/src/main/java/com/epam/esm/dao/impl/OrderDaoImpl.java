package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderWithCertificatesDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class OrderDaoImpl implements OrderDao {

  private final EntityManager entityManager;
  private final PaginationHandler paginationHandler;

  public OrderDaoImpl(EntityManager entityManager, PaginationHandler paginationHandler) {
    this.entityManager = entityManager;

    this.paginationHandler = paginationHandler;
  }

  @Override
  public OrderWithCertificatesWithTagsForCreationDto create(
      OrderWithCertificatesWithTagsForCreationDto dto) {
    Order order = new Order(dto);
    entityManager.persist(order);

    order.getCertificates().stream()
        .map(certificate -> entityManager.find(Certificate.class, certificate.getId()))
        .forEach(certificate -> certificate.setOrder(order));

    return new OrderWithCertificatesWithTagsForCreationDto(order);
  }

  @Override
  public PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter) {
    Session session = entityManager.unwrap(Session.class);
    Optional.ofNullable(session.get(User.class, userId))
        .orElseThrow(ResourceValidationException.validationWithUser(userId));

    Query<Order> query = session.createQuery("From Order where user.id=:user");
    query.setParameter("user", userId);
    long numberOfElements = query.getResultStream().count();
    long numberOfPages =
        paginationHandler.calculateNumberOfPages(numberOfElements, parameter.getSize());
    paginationHandler.setPageToQuery(query, parameter);
    List<OrderDto> orders = query.list().stream().map(OrderDto::new).collect(Collectors.toList());

    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, orders);
  }

  @Override
  public Optional<OrderWithCertificatesDto> readOrderByUser(long userId, long orderId) {
    Optional.ofNullable(entityManager.find(User.class, userId))
        .orElseThrow(ResourceValidationException.validationWithUser(userId));

    Optional<Order> order = Optional.ofNullable(entityManager.find(Order.class, orderId));
    return order.map(OrderWithCertificatesDto::new);
  }
}

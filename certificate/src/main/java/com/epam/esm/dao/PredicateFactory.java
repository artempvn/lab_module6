package com.epam.esm.dao;

import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.entity.Certificate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/** The interface Predicate factory. */
public interface PredicateFactory {

  /**
   * Build predicates list.
   *
   * @param builder the builder
   * @param request the request contains sorting and filtering staff
   * @param root the root
   * @return the list
   */
  List<Predicate> buildPredicates(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root);
}

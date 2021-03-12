package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificateWithTagsDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

  private final EntityManager entityManager;

  public CertificateDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public CertificateWithTagsDto create(CertificateWithTagsDto certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    entityManager.persist(certificate);

    certificate.getTags().forEach(entityManager::merge);
    certificate.getTags().forEach(tag -> tag.withCertificate(certificate));

    return new CertificateWithTagsDto(certificate);
  }
}

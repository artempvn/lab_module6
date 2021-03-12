package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderWithCertificatesWithTagsForCreationDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime createDate;
  @Valid @NotEmpty private List<CertificateWithTagsDto> certificates = Collections.emptyList();

  public OrderWithCertificatesWithTagsForCreationDto() {}

  public OrderWithCertificatesWithTagsForCreationDto(Order entity) {
    this.id = entity.getId();
    this.userId = entity.getUser().getId();
    this.createDate = entity.getCreateDate();
    this.certificates =
        entity.getCertificates().stream()
            .map(CertificateWithTagsDto::new)
            .collect(Collectors.toList());
  }

  private OrderWithCertificatesWithTagsForCreationDto(Builder builder) {
    id = builder.id;
    userId = builder.userId;
    createDate = builder.createDate;
    certificates = builder.certificates;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public List<CertificateWithTagsDto> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateWithTagsDto> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderWithCertificatesWithTagsForCreationDto that =
        (OrderWithCertificatesWithTagsForCreationDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
    if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null)
      return false;
    return certificates != null
        ? certificates.equals(that.certificates)
        : that.certificates == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderDtoFullCreation{");
    sb.append("id=").append(id);
    sb.append(", userId=").append(userId);
    sb.append(", createDate=").append(createDate);
    sb.append(", certificates=").append(certificates);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private Long userId;
    private LocalDateTime createDate;
    private List<CertificateWithTagsDto> certificates = Collections.emptyList();

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public Builder certificates(List<CertificateWithTagsDto> certificates) {
      this.certificates = certificates;
      return this;
    }

    public OrderWithCertificatesWithTagsForCreationDto build() {
      return new OrderWithCertificatesWithTagsForCreationDto(this);
    }
  }
}

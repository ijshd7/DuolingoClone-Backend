package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "path_icons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathIcon {
  @Id
  @Column(name = "unit_id")
  private Integer unitId;

  @Column(name = "icon", nullable = false)
  private String icon;
}

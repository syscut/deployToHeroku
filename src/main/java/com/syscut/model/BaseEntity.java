package com.syscut.model;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.StringArrayType;


@TypeDef(name = "string-array",typeClass = StringArrayType.class)
@MappedSuperclass
public class BaseEntity {

}

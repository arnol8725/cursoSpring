package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer cantidad;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="producto_id")
	private Producto producto;
	
	
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ItamFactura [id=" + id + ", cantidad=" + cantidad + "]";
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}
	public Double calcularImporte() {
		return cantidad.doubleValue()* producto.getPrecio();
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
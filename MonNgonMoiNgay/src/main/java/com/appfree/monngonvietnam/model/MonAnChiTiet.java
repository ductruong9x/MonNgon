package com.appfree.monngonvietnam.model;

public class MonAnChiTiet {
	public int id;
	public String tenmonan;
	public String anhmonan;
	public String kieumon;
	public String nguyenlieu;
	public String congthuc;
	public double kinhdo, vido;
	public String search;
	public String vungmien;
	public String diachi;
	public MonAnChiTiet(int id, String tenmonan, String anhmonan,
			String kieumon, String nguyenlieu, String congthuc, double kinhdo,
			double vido, String search,String vungmien,String diachi) {
		this.id = id;
		this.tenmonan = tenmonan;
		this.anhmonan = anhmonan;
		this.kieumon = kieumon;
		this.nguyenlieu = nguyenlieu;
		this.congthuc = congthuc;
		this.kieumon = kieumon;
		this.vido = vido;
		this.search = search;
		this.vungmien=vungmien;
		this.diachi=diachi;
	}

	public String getDiachi() {
		return diachi;
	}

	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getTenMonAn() {
		return tenmonan;
	}

	public void setTenMonAn(String tenmonan) {
		this.tenmonan = tenmonan;
	}

	public String getAnhMonAn() {
		return anhmonan;
	}

	public void setAnhMonAn(String anhmonan) {
		this.anhmonan = anhmonan;
	}

	public String getKieuMon() {
		return kieumon;
	}

	public void setKieuMon(String kieumon) {
		this.kieumon = kieumon;
	}

	public String getNguyenLieu() {
		return nguyenlieu;
	}

	public void setNguyenLieu(String nguyenlieu) {
		this.nguyenlieu = nguyenlieu;
	}

	public String getCongThuc() {
		return congthuc;
	}

	public void setCongThuc(String congthuc) {
		this.congthuc = congthuc;
	}

	public double getKinhDo() {
		return kinhdo;
	}

	public void setKinhDo(double kinhdo) {
		this.kinhdo = kinhdo;
	}

	public double getViDo() {
		return vido;
	}

	public void setViDo(double vido) {
		this.vido = vido;
	}

	public String getVungmien() {
		return vungmien;
	}

	public void setVungmien(String vungmien) {
		this.vungmien = vungmien;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}

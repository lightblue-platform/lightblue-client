package com.redhat.lightblue.client.http.model;

public class SimpleModelObject {

	String _id = "", field = "";

	public SimpleModelObject() {
	}

	public SimpleModelObject(String _id, String field) {
		super();
		this._id = _id;
		this.field = field;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public boolean equals(Object obj) {
		SimpleModelObject o = (SimpleModelObject) obj;

		return _id.equals(o._id) && field.equals(o.field);
	}

}

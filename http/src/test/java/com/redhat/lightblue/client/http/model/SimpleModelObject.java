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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleModelObject)) return false;

        SimpleModelObject that = (SimpleModelObject) o;

        if (!_id.equals(that._id)) return false;
        if (!field.equals(that.field)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }
}

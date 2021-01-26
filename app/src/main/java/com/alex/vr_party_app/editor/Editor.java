package com.alex.vr_party_app.editor;

import android.os.Parcel;
import android.os.Parcelable;

public class Editor implements Parcelable {

    private String id;
    private String editor;
    private String name;

    public Editor() {
    }

    public Editor(String id, String editor, String name) {
        this.id = id;
        this.editor = editor;
        this.name = name;
    }

    protected Editor(Parcel in) {
        id = in.readString();
        editor = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(editor);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Editor> CREATOR = new Creator<Editor>() {
        @Override
        public Editor createFromParcel(Parcel in) {
            return new Editor(in);
        }

        @Override
        public Editor[] newArray(int size) {
            return new Editor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "id='" + id + '\'' +
                ", editor='" + editor + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

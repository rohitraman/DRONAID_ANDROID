package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/21/17.
 */

public class Username
{
    private String softDelete;

    private String username;

    private String _id;

    private String acctype;

    private String password;

    public String getSoftDelete ()
    {
        return softDelete;
    }

    public void setSoftDelete (String softDelete)
    {
        this.softDelete = softDelete;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getAcctype ()
    {
        return acctype;
    }

    public void setAcctype (String acctype)
    {
        this.acctype = acctype;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [softDelete = "+softDelete+", username = "+username+", _id = "+_id+", acctype = "+acctype+", password = "+password+"]";
    }
}
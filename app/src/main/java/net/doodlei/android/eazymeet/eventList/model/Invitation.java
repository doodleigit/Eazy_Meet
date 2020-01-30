package net.doodlei.android.eazymeet.eventList.model;

public class Invitation {

    private String text, id, invitationIdentityId, acceptStatus, type, firstName, lastName;

    public Invitation(String text, String id, String invitationIdentityId, String acceptStatus, String type, String firstName, String lastName) {
        this.text = text;
        this.id = id;
        this.invitationIdentityId = invitationIdentityId;
        this.acceptStatus = acceptStatus;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvitationIdentityId() {
        return invitationIdentityId;
    }

    public void setInvitationIdentityId(String invitationIdentityId) {
        this.invitationIdentityId = invitationIdentityId;
    }

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

package net.doodlei.android.eazymeet.eventList.service;

public interface InvitationClickEventListener {

    void onAcceptClick(String invitationIdentityId, String status);
    void onRejectClick(String invitationIdentityId, String status);
    void onViewOnMapClick(String invitationIdentityId);

}

package net.doodlei.android.eazymeet.shareLocation.service;

import net.doodlei.android.eazymeet.shareLocation.model.Contact;

public interface ContactSelectListener {

    void onContactCheck(Contact contact);
    void onContactUncheck(Contact contact);

}

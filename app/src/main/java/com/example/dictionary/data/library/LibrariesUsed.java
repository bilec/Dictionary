package com.example.dictionary.data.library;

import com.example.dictionary.data.licence.LicencesUsed;

import java.util.ArrayList;
import java.util.List;

public class LibrariesUsed {

    public static List<Library> getAllLibrariesUsed()
    {
        List<Library> usedLibraries = new ArrayList<>();

        usedLibraries.add(new Library("AndroidX Appcompat", LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX ConstraintLayout",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX Lifecycle",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX Navigation",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX Preference",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX RecyclerView",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX Room",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("AndroidX WorkManager",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("Gson",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("material-components-android",LicencesUsed.getALv2Licence()));
        usedLibraries.add(new Library("jsoup",LicencesUsed.getMITLicence("2009 - 2020","Jonathan Hedley")));
        usedLibraries.add(new Library("material-components-android",LicencesUsed.getALv2Licence()));

        return usedLibraries;
    }


}

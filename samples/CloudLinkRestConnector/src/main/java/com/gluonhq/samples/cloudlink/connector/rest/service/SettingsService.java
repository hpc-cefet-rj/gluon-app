package com.gluonhq.samples.cloudlink.connector.rest.service;

import com.gluonhq.samples.cloudlink.connector.rest.model.Settings;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * An EJB Session bean that works as a service class for managing the Settings object in the configured JPA database.
 */
@Stateless
public class SettingsService {

    private static final String SETTINGS_ID = "SETTINGS_IDENTIFIER";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find the settings object.
     *
     * @return the default Settings object
     */
    public Settings findSettings() {
        Settings settings = entityManager.find(Settings.class, SETTINGS_ID);
        if (settings == null) {
            settings = new Settings();
            settings.setId(SETTINGS_ID);
            settings.setShowDate(true);
            settings.setAscending(true);
            settings.setSortingId(0);
            settings.setFontSize(10);
            entityManager.persist(settings);
        }
        return settings;
    }

    /**
     * Update the Settings object with the provided parameters.
     *
     * @param showDate whether to show the data or not
     * @param ascending whether to sort ascending or descending
     * @param sortingId the id of the sort method: 0 = DATE, 1 =TITLE or 2 = CONTENT
     * @param fontSize the font size to use for displaying the notes
     * @return the merged Settings
     */
    public Settings updateSettings(boolean showDate, boolean ascending, int sortingId, int fontSize) {
        Settings settings = findSettings();
        settings.setShowDate(showDate);
        settings.setAscending(ascending);
        settings.setSortingId(sortingId);
        settings.setFontSize(fontSize);
        return entityManager.merge(settings);
    }
}

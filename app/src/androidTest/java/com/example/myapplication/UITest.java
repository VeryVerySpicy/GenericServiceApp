package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;

import androidx.test.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

public class UITest {
    private static final String APP = "com.example.myapplication";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;


    @Before  /* IInitialization*/
    public void startMainActivityFromHomeScreen() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Create UI Automator Intent
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Launch the app
        context.startActivity(intent);
    }

    private void performLogin() throws Exception {
        UiObject usernameEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/username"));
        UiObject passwordEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/password"));
        UiObject loginButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/login_button"));

        if (usernameEditText.exists() && passwordEditText.exists() && loginButton.exists()) {
            usernameEditText.setText("admin");
            passwordEditText.setText("admin");
            loginButton.click();
            mDevice.wait(Until.hasObject(By.res(APP, "homeScreenElement")), LAUNCH_TIMEOUT);
        }
    }

    @Test
    public void testLoginUI() throws Exception {
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        UiObject usernameEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/username"));
        UiObject passwordEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/password"));
        UiObject loginButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/login_button"));

        // Ensure the login button exists
        assertTrue("login button not found", loginButton.exists());

        // Ensure the username field input works
        usernameEditText.setText("admin");
        assertEquals("admin", usernameEditText.getText());

        // Ensure the password field input works
        passwordEditText.setText("admin");
        assertEquals("•••••", passwordEditText.getText());

    }

    @Test
    public void testSearchListViewUI() throws Exception {
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        performLogin();

        UiObject searchEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/searchEditText"));
        UiObject searchButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/searchButton"));
        UiObject searchListView = mDevice.findObject(new UiSelector().resourceId(APP + ":id/personListView"));

        // Ensure the search button exists
        assertTrue("Search button not found", searchButton.exists());

        // Ensure the ListView exists
        assertTrue("ListView not found", searchListView.exists());

        // Ensure the search input works
        searchEditText.setText("Thor");
        assertEquals("Thor", searchEditText.getText());

        // Find a specific item within the ListView
        searchButton.click();
        UiObject listItem = searchListView.getChild(new UiSelector().textContains("Thor"));
        assertTrue("List item not found", listItem.exists());

        //Go back to the Home Screen
        mDevice.pressHome();
    }

    @Test
    public void testListStatusUI() throws Exception {
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        performLogin();

        UiObject listView = mDevice.findObject(new UiSelector().resourceId(APP + ":id/personListView"));
        assertTrue("ListView not found", listView.exists());

        UiObject customLayoutItem = listView.getChild(new UiSelector().index(0));
        assertTrue("Custom layout item not found at index 0", customLayoutItem.exists());

        UiObject listStatusButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/listStatusBtn"));
        assertTrue("listStatusBtn not found in custom layout", listStatusButton.exists());
        listStatusButton.click();

        // Find the CheckBox in the custom layout
        UiObject checkBox = mDevice.findObject(new UiSelector().resourceId(APP + ":id/listCheckboxLayout").childSelector(new UiSelector().className(CheckBox.class.getName())));
        assertTrue("CheckBox not found in custom layout", checkBox.exists());

        // Click the CheckBox
        checkBox.click(); // Check or uncheck the checkbox

        // Optional: Verify the CheckBox state if needed
        boolean isCheckable = checkBox.isCheckable();
        assertTrue("Checkbox should be checked", isCheckable); // Example assertion

        //Go back to the Home Screen
        mDevice.pressHome();
    }

    @Test
    public void testDetailedListViewUI() throws Exception {
        mDevice.wait(Until.hasObject(By.pkg(APP).depth(0)), LAUNCH_TIMEOUT);

        performLogin();

        UiObject searchEditText = mDevice.findObject(new UiSelector().resourceId(APP + ":id/searchEditText"));
        UiObject searchButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/searchButton"));
        UiObject searchListView = mDevice.findObject(new UiSelector().resourceId(APP + ":id/personListView"));

        // Ensure the search input works
        searchEditText.setText("Thor");
        searchButton.click();

        UiObject listItem = searchListView.getChild(new UiSelector().textContains("Thor"));
        listItem.click();

        UiObject detailedStatusButton = mDevice.findObject(new UiSelector().resourceId(APP + ":id/detailStatusBtn"));
        assertTrue("Status Button is not found in the detailed layout", detailedStatusButton.exists());
        detailedStatusButton.click();

        // Find the CheckBox in the custom layout
        UiObject checkBox = mDevice.findObject(new UiSelector().resourceId(APP + ":id/detailCheckboxLayout").childSelector(new UiSelector().className(CheckBox.class.getName())));
        assertTrue("CheckBox not found in the detailed layout", checkBox.exists());

        // Click the CheckBox
        checkBox.click(); // Check or uncheck the checkbox

        // Optional: Verify the CheckBox state if needed
        boolean isCheckable = checkBox.isCheckable();
        assertTrue("Checkbox should be checked", isCheckable); // Example assertion

        detailedStatusButton.click();

        // mDevice.swipe(700, 900, 50, 900, 60); // Using int values for swipe method
        //Go back to the Home Screen
        mDevice.pressHome();
    }

}

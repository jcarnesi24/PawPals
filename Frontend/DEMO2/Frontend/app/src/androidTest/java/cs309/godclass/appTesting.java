package cs309.godclass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.IdRes;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import cs309.godclass.Screens.DogListScreen;
import cs309.godclass.Screens.RegistrationScreen;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class appTesting {
    private static final int SIMULATED_DELAY_MS = 5000;
    @Rule
    public ActivityTestRule<RegistrationScreen> mActivityRule = new ActivityTestRule<>(RegistrationScreen.class);

    @Before
    public void setUp() {
        // Mock the image picker intent
        Intents.init();

        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        // Set a default image URI for testing
        Uri defaultImageUri = Uri.parse("android.resource://cs309.godclass/" + R.drawable.small_dog);
        DogListScreen.setDefaultImageUriForTesting(defaultImageUri);




    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void navigateToSelectionScreen() {
        // Perform an action that triggers the navigation, for example, clicking a button
        String username = "test1";
        String password = "test1";

        // Type in testString and send request
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        waitABit();
        onView(withId(R.id.registerSubmitButton)).perform(click());
        waitABit();
        // Checks to see if activity was switched to buyer profile.
        onView(withId(R.id.ToSwipingButton)).check(matches(isDisplayed()));

        // Turns on seller
        onView(withId(R.id.buttonSettingsBuyer)).perform(click());

        onView(withId(R.id.switchSeller)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        // Checks for buyer and seller buttons on selection screen
        onView(withId(R.id.buyerPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sellerPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sitterPage)).check(matches(not(isDisplayed())));

        //Turn seller off and turn sitter on
        onView(withId(R.id.buyerPage)).perform(click());
        onView(withId(R.id.buttonSettingsBuyer)).perform(click());
        onView(withId(R.id.switchSeller)).perform(click());
        onView(withId(R.id.switchSitter)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        // Check for buyer and sitter buttons on selection screen
        onView(withId(R.id.buyerPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sitterPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sellerPage)).check(matches(not(isDisplayed())));

        // Turn buyer off and sitter on
        onView(withId(R.id.buyerPage)).perform(click());
        onView(withId(R.id.buttonSettingsBuyer)).perform(click());
        onView(withId(R.id.switchBuyer)).perform(click());
        onView(withId(R.id.switchSeller)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        //Check for sitter and seller buttons on selection screen
        onView(withId(R.id.sellerPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sitterPage)).check(matches(isDisplayed()));
        onView(withId(R.id.buyerPage)).check(matches(not(isDisplayed())));

        // Turn all three options on
        onView(withId(R.id.sellerPage)).perform(click());
        onView(withId(R.id.buttonSettingsSeller)).perform(click());
        onView(withId(R.id.switchBuyer)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        // Check all three buttons are displayed
        onView(withId(R.id.sellerPage)).check(matches(isDisplayed()));
        onView(withId(R.id.sitterPage)).check(matches(isDisplayed()));
        onView(withId(R.id.buyerPage)).check(matches(isDisplayed()));

        // Keep only seller on
        onView(withId(R.id.sellerPage)).perform(click());
        onView(withId(R.id.buttonSettingsSeller)).perform(click());
        onView(withId(R.id.switchBuyer)).perform(click());
        onView(withId(R.id.switchSitter)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        // Check if it goes to seller screen
        onView(withId(R.id.dogViewButton)).check(matches(isDisplayed()));

        // Switch to only sitter screen
        onView(withId(R.id.buttonSettingsSeller)).perform(click());
        onView(withId(R.id.switchSeller)).perform(click());
        onView(withId(R.id.switchSitter)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();


        // Turn off sitter turn on buyer to reset the test.
        onView(withId(R.id.SitterChatButton)).check(matches(isDisplayed()));

        onView(withId(R.id.buttonSettingsSitter)).perform(click());
        onView(withId(R.id.switchBuyer)).perform(click());

        onView(withId(R.id.switchSitter)).perform(click());
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

    }

    @Test
    public void navigateToMessageScreen(){

        //Login
        String username = "test2";
        String password = "test2";

        // Type in testString and send request
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        //Go to buyer and click messages
        onView(withId(R.id.buyerPage)).perform(click());
        onView(withId(R.id.messageBtn1)).perform(click());

        // Check if we made it to message screen
        onView(withId(R.id.messageSearch)).check(matches(isDisplayed()));

        // Click back button to ensure we go back to buyer profile screen
        onView(withId(R.id.backToButton)).perform(click());
        onView(withId(R.id.ToSwipingButton)).check(matches(isDisplayed()));

        // Go back to selection screen
        onView(withId(R.id.backBtn)).perform(click());

        // Go to seller page
        onView(withId(R.id.sellerPage)).perform(click());
        onView(withId(R.id.sellerChatButton)).perform(click());

        // Check if we made it to message screen
        onView(withId(R.id.messageSearch)).check(matches(isDisplayed()));

        // Click back button to ensure we go back to seller profile screen
        onView(withId(R.id.backToButton)).perform(click());
        onView(withId(R.id.sellerChatButton)).check(matches(isDisplayed()));

        // Go back to selection screen
        onView(withId(R.id.sellerBackButton)).perform(click());

        // Go to sitter page
        onView(withId(R.id.sitterPage)).perform(click());
        onView(withId(R.id.SitterChatButton)).perform(click());

        // Check if we made it to message screen
        onView(withId(R.id.messageSearch)).check(matches(isDisplayed()));

        // Click back button to ensure we go back to seller profile screen
        onView(withId(R.id.backToButton)).perform(click());
        onView(withId(R.id.SitterChatButton)).check(matches(isDisplayed()));
    }
    @Test
    public void checkAddDogScreen() {
        String username = "test2";
        String password = "test2";


        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        //Go to the seller page
        onView(withId(R.id.sellerPage)).perform(click());

        //Click the view dogs button and ensure we are on the correct view
        onView(withId(R.id.dogViewButton)).perform(click());
        onView(withId(R.id.dogSearch)).check(matches(isDisplayed()));

        //Click add dog button and ensure text fields for name, age, and breed are there.
        onView(withId(R.id.addDog)).perform(click());
        onView(withId(R.id.editDogName)).check(matches(isDisplayed()));
        onView(withId(R.id.editDogAge)).check(matches(isDisplayed()));
        onView(withId(R.id.editDogBreed)).check(matches(isDisplayed()));

        //Values for the dog
        String name = "teddy";
        String age = "3";
        String breed = "Dog";

        // Enter the name, age, and breed into the text boxes and hit the save button.
        onView(withId(R.id.editDogName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.editDogAge)).perform(typeText(age), closeSoftKeyboard());
        onView(withId(R.id.editDogBreed)).perform(typeText(breed), closeSoftKeyboard());
        //onView(withId(R.id.chooseImageButtonDog)).perform(click());
        onView(withId(R.id.saveDogButton)).perform(click());

        //Make sure we made it back to the dog list screen.
        onView(withId(R.id.dogSearch)).check(matches(isDisplayed()));

        // This will check to see if the linear layout has something in it meaning the dog was added.
        onView(withId(R.id.dogLayout)).check(matches(hasChildCount(greaterThan(0))));

        // Go back to sitter page
        onView(withId(R.id.backToSellerButton)).perform(click());

        //Click the view dogs button and ensure we are on the correct view
        onView(withId(R.id.logoutButton)).perform(click());


        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        //Go to the seller page
        onView(withId(R.id.sellerPage)).perform(click());

        //Click the view dogs button and ensure we are on the correct view
        onView(withId(R.id.dogViewButton)).perform(click());
        onView(withId(R.id.dogSearch)).check(matches(isDisplayed()));

        // Checks that after logging out and back in that the dog is still there
        onView(withId(R.id.dogLayout)).check(matches(hasChildCount(greaterThan(0))));

        String buttonToClick = "View This Dog";

        // Checks to see if there is a button called View This Dog
        onView(withId(R.id.dogLayout)).check(matches(hasDescendant(withText(buttonToClick))));

        // Clicks the View This Dog Button
        onView(withText(buttonToClick)).perform(click());

        //Ensures we are back to the add/edit screen
        onView(withId(R.id.editDogName)).check(matches(isDisplayed()));
        onView(withId(R.id.editDogAge)).check(matches(isDisplayed()));
        onView(withId(R.id.editDogBreed)).check(matches(isDisplayed()));


        // Clicks the delete button
        onView(withId(R.id.deleteDogButton)).perform(click());

        //Make sure we made it back to the dog list screen.
        onView(withId(R.id.dogSearch)).check(matches(isDisplayed()));

        //Checks that nothing is in the dog layout meaning the dog was properly removed
        onView(withId(R.id.dogLayout)).check(matches(hasChildCount(equalTo(0))));

    }

    @Test
    public void checkCreateUser(){
        String firstname = "test3";
        String lastname = "test3";
        String email = "test3";
        String username = "test3";
        String password = "test3";
        String address = "test3";

        // Sign up button is clicked
        onView(withId(R.id.signInButton)).perform(click());

        // Create a new profile
        onView(withId(R.id.EditFirstname)).perform(typeText(firstname), closeSoftKeyboard());
        onView(withId(R.id.EditLastname)).perform(typeText(lastname), closeSoftKeyboard());
        onView(withId(R.id.EditEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.EditUsername)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.EditPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.EditAddress)).perform(typeText(address), closeSoftKeyboard());
        waitABit();
        onView(withId(R.id.submitButton)).perform(click());
        waitABit();
        // Check if you are on registration screen
        onView(withId(R.id.nameTextField)).check(matches(isDisplayed()));

        // Login with the new account
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        waitABit();
        onView(withId(R.id.registerSubmitButton)).perform(click());
        waitABit();

        // Go to the buyer settings page
        onView(withId(R.id.buttonSettingsBuyer)).perform(click());

        // Hit the delete profile button
        waitABit();
        onView(withId(R.id.deleteButtonSettings)).perform(click());
        waitABit();

        // Check that we are back at the registration screen
        onView(withId(R.id.nameTextField)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwipingDog(){
        String username = "test2";
        String password = "test2";

        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        //Go to the buyer page
        onView(withId(R.id.buyerPage)).perform(click());

        // Go to dog swiping screen
        onView(withId(R.id.ToSwipingButton)).perform(click());

        //Ensure that we made it to dog swipeing screen
        onView(withId(R.id.SWIPENAME)).check(matches(isDisplayed()));

        // Checks to see if the text boxes are filled with the correct values and collects the values
        String name = Espresso.onView(ViewMatchers.withId(R.id.SWIPENAME)).toString();
        String breed = Espresso.onView(ViewMatchers.withId(R.id.SWIPEBREED)).toString();
        String age = Espresso.onView(ViewMatchers.withId(R.id.SWIPEAGE)).toString();
        String bio = Espresso.onView(ViewMatchers.withId(R.id.swipeDogBioTextView)).toString();

        // Clicks yes to ensure that it goes to another dog after
        onView(withId(R.id.YesButton)).perform(click());

        // Checks to make sure duke is not still being displayed
       checkDog(name,bio,age,breed);

        // Clicks the No button to make sure it works
        onView(withId(R.id.NoButton)).perform(click());

        // Again checks that the next dog is not duke
        checkDog(name,bio,age,breed);
    }

    @Test
    public void testSettingsPage() {
        String username = "test2";
        String password = "test2";

        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.buyerPage)).perform(click());

        onView(withId(R.id.buttonSettingsBuyer)).perform(click());
        onView(withId(R.id.editUsernameText)).perform(typeText("a"), closeSoftKeyboard());
        username += "a";
        onView(withId(R.id.editTextPassword)).perform(typeText("a"), closeSoftKeyboard());
        password += "a";
        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

        if(isButtonDisplayed(R.id.deleteButtonSettings)) {
            onView(withId(R.id.backButtonSettings)).perform(click());
            onView(withId(R.id.backBtn)).perform(click());
        }

        onView(withId(R.id.buyerPage)).perform(click());
        onView(withId(R.id.logoutButton)).perform(click());

        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.buyerPage)).check(matches(isDisplayed()));

        onView(withId(R.id.buyerPage)).perform(click());

        onView(withId(R.id.buttonSettingsBuyer)).perform(click());
        onView(withId(R.id.editUsernameText)).perform(replaceText("test2"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(replaceText("test2"), closeSoftKeyboard());

        waitABit();
        onView(withId(R.id.SaveButtonSettings)).perform(click());
        waitABit();

       }

    @Test
    public void preferenceTest() {
        String username = "test2";
        String password = "test2";

        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.buyerPage)).perform(click());

        onView(withId(R.id.selectInsideDogSwitch)).perform(click());
        onView(withId(R.id.selectPottyTrainedSwitch)).perform(click());
        waitABit();
        onView(withId(R.id.saveFiltersButton)).perform(click());
        waitABit();

        onView(withId(R.id.selectBreedFilter)).perform(click());

        onView(withId(R.id.editTextBreedFilter)).check(matches(isDisplayed()));

        onView(withId(R.id.logoutButton)).perform(click());

        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.buyerPage)).perform(click());

        onView(withId(R.id.selectPottyTrainedSwitch)).check(matches(isChecked()));
        onView(withId(R.id.selectInsideDogSwitch)).check(matches(isChecked()));

        onView(withId(R.id.selectInsideDogSwitch)).perform(click());
        onView(withId(R.id.selectPottyTrainedSwitch)).perform(click());
        waitABit();
        onView(withId(R.id.saveFiltersButton)).perform(click());
        waitABit();
    }

    @Test
    public void ratingTest() {
        String username = "test2";
        String password = "test2";
        float expectedRating = (float) 5.0f;

        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.sitterPage)).perform(click());

        onView(withId(R.id.ratingBar2)).check(matches(isDisplayed()));
        onView(withId(R.id.ratingBar2)).check(matches(withRating(expectedRating)));

        onView(withId(R.id.SitterBackButton)).perform(click());
        onView(withId(R.id.sellerPage)).perform(click());

        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        onView(withId(R.id.ratingBar)).check(matches(withRating(expectedRating)));



    }

    @Test
    public void sitterTest() {
        String username = "test2";
        String password = "test2";

        // Login as test2
        onView(withId(R.id.nameTextField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.registerSubmitButton)).perform(click());

        onView(withId(R.id.buyerPage)).perform(click());
        onView(withId(R.id.toSwipingButtonSitter)).perform(click());

        onView(withId(R.id.YesSitterSwipe)).check(matches(isDisplayed()));


    }


    public void checkDog(String name, String bio, String age, String breed){
        boolean nameFailed = false;
        boolean breedFailed = false;
        boolean bioFailed = false;
        boolean ageFailed = false;

        try {
            onView(withId(R.id.SWIPENAME)).check(matches(not(withText(name))));
        } catch (NoMatchingViewException e) {
            nameFailed = true;
        }

        try {
            onView(withId(R.id.swipeDogBioTextView)).check(matches(not(withText(bio))));
        } catch (NoMatchingViewException e) {
            bioFailed = true;
        }

        try {
            onView(withId(R.id.SWIPEBREED)).check(matches(not(withText(breed))));
        } catch (NoMatchingViewException e) {
            breedFailed = true;
        }

        try {
            onView(withId(R.id.SWIPEAGE)).check(matches(not(withText(age))));
        } catch (NoMatchingViewException e) {
            ageFailed = true;
        }

        //If all of the checks failed it means that the dog did not switch and we want to fail this test
        // Since a dog can have the same name, breed, age, bio as another dog we need to make
        // sure all of these fail before failing the test. It is unlikely that there is an exact same dog.
        if(nameFailed && bioFailed && breedFailed && ageFailed){
            assertFalse(nameFailed);
        }
    }

    public void waitABit(){
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

    }

    public static Matcher<View> hasChildCount(final Matcher<Integer> integerMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view instanceof ViewGroup && integerMatcher.matches(((ViewGroup) view).getChildCount());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has child count: ");
                integerMatcher.describeTo(description);
            }
        };

    }


        public Matcher<Object> withRating(final float rating) {
            return new BoundedMatcher<Object, RatingBar>(RatingBar.class) {
                @Override
                protected boolean matchesSafely(RatingBar ratingBar) {
                    return ratingBar.getRating() == rating;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("with rating: " + rating);
                }
            };
    }


    private boolean isButtonDisplayed(@IdRes int buttonId) {
        try {
            onView(withId(buttonId)).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException e) {
            return false;
        }
    }



}
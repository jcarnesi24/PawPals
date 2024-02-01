package cs309.godclass.Screens;

import cs309.godclass.Objects.Profile;

/**
 * The IView interface serves as a contract for classes that require a standard set of methods for handling view-related actions.
 */
public interface IView {
    /**
     * Displays text in the user interface.
     *
     * @param s The text to be displayed.
     */
    public void showText (String s);
    /**
     * Displays text as a toast message in the user interface.
     *
     * @param s The text to be displayed as a toast.
     */
    public void toastText (String s);
    /**
     * Sets the user profile in the view.
     *
     * @param profile The user's profile to be set in the view.
     */
    public void setProfile (Profile profile);
}

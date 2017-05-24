package hackerman.notebookmushroom.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import hackerman.notebookmushroom.UI.fragment.note.AddNoteFragment;
import hackerman.notebookmushroom.UI.fragment.mushroom.MushroomDetailFragment;
import hackerman.notebookmushroom.UI.fragment.noteviewer.NoteViewerFragment;
import hackerman.notebookmushroom.UI.fragment.phototodoobj.PhotoViewerFragment;
import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.UI.fragment.descriptionmushroom.DescriptionMushroomFragment;
import hackerman.notebookmushroom.UI.fragment.places.AddPlacesFragment;
import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.db.MushroomObj;
import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.interactions.AddDescriptionMushroom;
import hackerman.notebookmushroom.interactions.AddNoteIteraction;
import hackerman.notebookmushroom.interactions.AddPlaceInteraction;
import hackerman.notebookmushroom.interactions.DrawerChangeStateInteraction;
import hackerman.notebookmushroom.interactions.MushroomPhotoViwerIteraction;
import hackerman.notebookmushroom.interactions.NoteAddTodoObj;

public class
MainActivity extends AppCompatActivity
        implements DrawerChangeStateInteraction, AddPlaceInteraction, AddNoteIteraction, NoteAddTodoObj, AddDescriptionMushroom, MushroomPhotoViwerIteraction {
    private Drawer drawer;
    private String photo;
    private final static int REQUEST_GALLERY = 1000;
    private AccountHeader accountHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isPanelFragmentEmpty()) {
            initStartFragment();
        }
        initDrawer();
    }

    private boolean isPanelFragmentEmpty() {
        return getSupportFragmentManager().findFragmentByTag("panel1") == null;
    }

    private void initStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frl_container, AddPlacesFragment.newInstance(), "panel1")
                .commit();
    }

    public void initDrawer() {
        final PrimaryDrawerItem drawerItem0 = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_notes)
                .withIcon(R.drawable.ic_description_black_24dp);
        final SecondaryDrawerItem secondaryDrawerItem = new SecondaryDrawerItem()
                .withName(R.string.drawer_item_gallery)
                .withIcon(R.drawable.ic_developer_board_black_24dp);
        final SecondaryDrawerItem secondaryDrawerItem1 = new SecondaryDrawerItem()
                .withName("Add profile")
                .withIcon(R.drawable.ic_face_black_24dp);

        final AccountHeader accountHeader = getAccountHeader();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .addDrawerItems(drawerItem0, new DividerDrawerItem()
                        , secondaryDrawerItem, secondaryDrawerItem1)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch (position) {
                        case 1: {
                            addDescriptionMushroom();
                            drawer.closeDrawer();
                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/");
                            startActivity(intent);
                            drawer.closeDrawer();
                            break;
                        }
                        case 4: {
                            updateAccountProfile();
                            break;
                        }
                    }
                    return true;
                })
                .build();
    }

    private void addDescriptionMushroom() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frl_container, MushroomDetailFragment.newInstance(), "mushroom").addToBackStack("mushroom")
                .commit();
    }

    private void updateAccountProfile() {
        new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.dialog_two_edit_text, true)
                .positiveText("Ok")
                .onPositive((dialog, which) -> {
                    EditText etEmail = (EditText) dialog.findViewById(R.id.et_email);
                    EditText etName = (EditText) dialog.findViewById(R.id.et_name);
                    String newEmail = etEmail.getText().toString();
                    String newName = etName.getText().toString();
                    drawer.closeDrawer();
                    if (newEmail != null || newName != null) {
                        App.getAppPreferences().putString("email", newEmail);
                        App.getAppPreferences().putString("name", newName);

                        final IProfile activeProfile = accountHeader.getActiveProfile();
                        activeProfile.withName(newName);
                        activeProfile.withEmail(newEmail);
                        accountHeader.updateProfile(activeProfile);

                    }

                })
                .show();
    }


    private AccountHeader getAccountHeader() {
        final String emailProfile = App.getAppPreferences().getString("email", " ");
        final String nameProfile = App.getAppPreferences().getString("name", " ");
        photo = App.getAppPreferences().getString("photo", "");
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header2)
                .addProfiles(new ProfileDrawerItem()
                        .withName(nameProfile)
                        .withEmail(emailProfile)
                        .withIcon(photo)
                        .withSelectedTextColor(getResources().getColor(R.color.cardview_dark_background)))

                .withOnAccountHeaderListener((view, profile, current) -> {
                    addNewProfilePhoto();
                    drawer.closeDrawer();
                    return true;
                })
                .build();
        return accountHeader;
    }

    private void addNewProfilePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GALLERY: {
                    if (data != null) {
                        final Uri uri = data.getData();
                        App.getAppPreferences().putString("photo", getPathFromUri(uri));
                        final IProfile activeProfile = accountHeader.getActiveProfile();
                        activeProfile.withIcon(uri);
                        accountHeader.updateProfile(activeProfile);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void openDrawer() {
        drawer.openDrawer();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer();
    }

    @Override
    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen();
    }

    @Override
    public void invertDrawerState() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    @Override
    public void openTodoPhotoInViewer(final TodoObj todoObj) {
        new Thread(() -> {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right)
                    .replace(R.id.frl_container, PhotoViewerFragment.newInstance(todoObj), "PhotoViewerFragment").addToBackStack("PhotoViewerFragment")
                    .commit();
        }).start();

    }

    @Override
    public void openNoteTodoObj(TodoObj todoObj) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frl_container, NoteViewerFragment.newInstance(todoObj), "AddNoteFragment").addToBackStack("AddNoteFragment")
                .commit();
    }

    @Override
    public void newNoteTodoObj(TodoObj todoObj) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frl_container, AddNoteFragment.newInstance(todoObj), "Add Note").addToBackStack("Add Note")
                .commit();
    }

    @Override
    public void openDescriptionMushroom(MushroomObj mushroomObj) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.frl_container, DescriptionMushroomFragment.newInstance(mushroomObj), "Add description").addToBackStack("Add description")
                .commit();

    }

    @Override
    public void mushroomViwerPhoto(MushroomObj mushroomObj) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.frl_container, MushroomPhoto.newInstance(mushroomObj), "mushroom").addToBackStack("mushroom")
                .commit();
    }
    @Override
    protected void onDestroy() {

        getSupportFragmentManager().removeOnBackStackChangedListener(() -> invertDrawerState());

        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPathFromUri(Uri uri) {
        String filePath = null;
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split("=")[1];

            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getApplicationContext().getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            filePath = getPathFromGalleryV14(uri);
        }
        return filePath;
    }

    private String getPathFromGalleryV14(Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath = "";
        Cursor cursor = null;
        java.io.File file = null;

        switch (0) {
            case 0: {
                cursor = getApplicationContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 1: {
                cursor = getApplicationContext().getContentResolver().query(uri, new String[]{MediaStore.Video.VideoColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 2: {
                filePath = uri.getPath();
                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            default: {
                return null;
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (filePath != null && !TextUtils.isEmpty(filePath)) {
            filePath = filePath.trim();
        }
        return filePath;
    }


}

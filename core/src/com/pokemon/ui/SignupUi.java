package com.pokemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.FormValidator;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.pokemon.screen.MainMenuScreen;

public class SignupUi extends AbstractUi {
    private Stage stage;
    private MainMenuScreen mainMenuScreen;
    public SignupUi(final MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;

        stage = new Stage(new ScreenViewport());
        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Gdx.input.setInputProcessor(stage);
        stage.addActor(new SignupUiForm());
    }

    public void resize (int width, int height) {
        if (width == 0 && height == 0) return; //see https://github.com/libgdx/libgdx/issues/3673#issuecomment-177606278
        stage.getViewport().update(width, height, true);
        PopupMenu.removeEveryMenu(stage);
    }

    public void update () {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void dispose () {
        stage.dispose();
    }

    class SignupUiForm extends VisWindow {
        public SignupUiForm() {
            super("Sign Up");

            TableUtils.setSpacingDefaults(this);
            defaults().padRight(1);
            defaults().padLeft(1);
            columnDefaults(0).left();

            VisTextButton cancelButton = new VisTextButton("cancel");
            VisTextButton acceptButton = new VisTextButton("accept");

            final VisValidatableTextField idField = new VisValidatableTextField();
            final VisValidatableTextField passwordField = new VisValidatableTextField();
            final VisValidatableTextField confirmPasswordField = new VisValidatableTextField();

            idField.setMaxLength(15);

            passwordField.setPasswordMode(true);
            passwordField.setPasswordCharacter('*');
            passwordField.setMaxLength(15);

            confirmPasswordField.setPasswordMode(true);
            confirmPasswordField.setPasswordCharacter('*');
            confirmPasswordField.setMaxLength(15);

            VisLabel errorLabel = new VisLabel();
            errorLabel.setColor(Color.RED);

            VisTable buttonTable = new VisTable(true);
            buttonTable.add(errorLabel).expand().fill();
            buttonTable.add(cancelButton);
            buttonTable.add(acceptButton);

            add(new VisLabel("ID: "));
            add(idField).expand().fill();
            row();
            add(new VisLabel("PASSWORD: "));
            add(passwordField).expand().fill();
            row();
            add(new VisLabel("CONFIRM PASSWORD: "));
            add(confirmPasswordField).expand().fill();
            row();
            add(buttonTable).fill().expand().colspan(2).padBottom(3);

            SimpleFormValidator validator; //for GWT compatibility
            validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
            validator.setSuccessMessage("all good!");
            validator.notEmpty(idField, "ID cannot be empty");
            validator.notEmpty(passwordField, "Password cannot be empty");
            validator.notEmpty(confirmPasswordField, "Password cannot be empty");

            acceptButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!passwordField.getText().equals(confirmPasswordField.getText())) {
                        Dialogs.showOKDialog(getStage(), "message", "Password is not match!");
                    } else {
                        mainMenuScreen.createAccount(idField.getText(), passwordField.getText());
                        System.out.println("passwordField = " + passwordField.getText());
                        System.out.println("confirmPasswordField = " + confirmPasswordField.getText());
                        mainMenuScreen.popScreen();
                    }
                }
            });

            cancelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mainMenuScreen.popScreen();
                }
            });

            pack();
            setSize(getWidth() + 60, getHeight());
            centerWindow();
            //setPosition(getWidth()/2, getHeight()/2);
        }
    }
}


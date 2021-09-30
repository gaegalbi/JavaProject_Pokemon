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
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;

class LoginUiForm extends VisWindow {
    public LoginUiForm() {
        super("form validator");

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton cancelButton = new VisTextButton("cancel");
        VisTextButton acceptButton = new VisTextButton("accept");

        VisValidatableTextField firstNameField = new VisValidatableTextField();
        VisValidatableTextField lastNameField = new VisValidatableTextField();
        VisValidatableTextField age = new VisValidatableTextField();
        VisCheckBox termsCheckbox = new VisCheckBox("accept terms");

        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabel).expand().fill();
        buttonTable.add(cancelButton);
        buttonTable.add(acceptButton);

        add(new VisLabel("first name: "));
        add(firstNameField).expand().fill();
        row();
        add(new VisLabel("last name: "));
        add(lastNameField).expand().fill();
        row();
        add(new VisLabel("age: "));
        add(age).expand().fill();
        row();
        add(termsCheckbox).colspan(2);
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
        validator.setSuccessMessage("all good!");
        validator.notEmpty(firstNameField, "first name cannot be empty");
        validator.notEmpty(lastNameField, "last name cannot be empty");
        validator.notEmpty(age, "age cannot be empty");
        validator.integerNumber(age, "age must be a number");
        validator.valueGreaterThan(age, "you must be at least 18 years old", 18, true);
        validator.checked(termsCheckbox, "you must accept terms");

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialogs.showOKDialog(getStage(), "message", "you made it!");
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        centerWindow();
        //setPosition(getWidth()/2, getHeight()/2);
    }
}

public class LoginUi {
    private Stage stage;
    public LoginUi() {
        VisUI.load(VisUI.SkinScale.X1);

        stage = new Stage(new ScreenViewport());
        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Gdx.input.setInputProcessor(stage);
        stage.addActor(new LoginUiForm());
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
        VisUI.dispose();
        stage.dispose();
    }
}


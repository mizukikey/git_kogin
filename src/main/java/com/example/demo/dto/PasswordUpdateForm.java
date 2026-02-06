package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordUpdateForm {
	
    private int id;

    @NotBlank(message = "現在のパスワードを入力してください")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードを入力してください")
    private String newPassword;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;
    
	public PasswordUpdateForm() {
		super();
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public PasswordUpdateForm(int id, @NotBlank(message = "現在のパスワードを入力してください") String currentPassword,
			@NotBlank(message = "新しいパスワードを入力してください") String newPassword,
			@NotBlank(message = "確認用パスワードを入力してください") String confirmPassword) {
		super();
		this.id = id;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
    
}

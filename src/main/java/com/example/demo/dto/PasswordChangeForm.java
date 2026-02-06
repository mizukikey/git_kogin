package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeForm {
    @NotBlank(message = "現在のパスワードを入力してください")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードを入力してください")
    private String newPassword;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;
    
    
    
	public PasswordChangeForm() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public PasswordChangeForm(@NotBlank(message = "現在のパスワードを入力してください") String currentPassword,
			@NotBlank(message = "新しいパスワードを入力してください") String newPassword,
			@NotBlank(message = "確認用パスワードを入力してください") String confirmPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
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

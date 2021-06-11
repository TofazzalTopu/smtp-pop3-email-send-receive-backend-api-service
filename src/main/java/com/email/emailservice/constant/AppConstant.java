package com.email.emailservice.constant;

/**
 * @author Tofazzal
 */
public final class AppConstant {

    private AppConstant() {
    }

    public static final String API_VERSION = "v1";
    public static final String API_REVISION = "1.0";

    public static final String MAIL_FETCH_SUCCESS = "Mail fetched successfully.";
    public static final String MAIL_FORWARDED_SUCCESS = "Mail forwarded successfully.";
    public static final String MAIL_SENT_SUCCESS = "Mail sent successfully.";
    public static final String MAIL_REPLY_SUCCESS = "Mail replied successfully.";
    public static final String MAIL_COMPOSED_SUCCESS = "Mail composed successfully.";
    public static final String MAIL_DELETE_SUCCESS = "Mail deleted successfully.";
    public static final String MAIL_LABEL_UPDATE_SUCCESS = "Mail label updated successfully.";
    public static final String MAIL_NOT_FOUND = "Mail not found.";
    public static final String INCORRECT_USER_NAME_PASSWORD = "Mail Username or password incorrect.";
    public static final String MAIL_DOWNLOAD_SUCCESS = "Mail downloaded successfully.";
    public static final String MAIL_ATTACHMENT_NOT_FOUND = "File not found.";


    public static final String MAIL_RECEIVED_SUCCESS = "POP3: Mail received successfully.";
    public static final String MAIL_SAVE_SUCCESS = "POP3: Mail saved successfully.";

    public static final String MAIL_FILE_SAVED_SUCCESS = "File saved successfully.";
    public static final String MAIL_MESSAGE_BUILD_SUCCESS = "Message build successfully.";
    public static final String MAIL_CONTENT_BUILD_SUCCESS = "Mail content build successfully.";
    public static final String MAIL_META_DATA_BUILD_SUCCESS = "Mail meta data build successfully.";

    public static final String MAIL_EXCEPTION = "Exception => ";

    public static final String LABEL_SAVE_SUCCESS = "Label saved successfully.";
    public static final String LABEL_UPDATE_SUCCESS = "Label updated successfully.";
    public static final String LABEL_FETCH_SUCCESS = "Labels fetched successfully.";
    public static final String LABEL_DELETE_SUCCESS = "Label deleted successfully.";
    public static final String LABEL_NOT_FOUND = "Label not found.";
    public static final String ONE_OR_MORE_LABEL_NOT_FOUND = "One or more label is not found from given labels: ";
    public static final String LABEL_ALREADY_EXIST = "Label already exist.";
    public static final String UNAUTHORIZED_RESOURCES = "You are not authorized to access this resource.";

    public static final String EMAIL_ACCOUNTS_SAVE_SUCCESS = "Email account created successfully.";
    public static final String EMAIL_ACCOUNTS_UPDATE_SUCCESS = "Email account updated successfully.";
    public static final String EMAIL_ACCOUNTS_DELETE_SUCCESS = "Email account deleted successfully.";
    public static final String EMAIL_ACCOUNTS_NOT_FOUND = "Email account not found.";
    public static final String EMAIL_ACCOUNTS_ALREADY_EXISTS = "Email account already exists!";
    public static final String EMAIL_ACCOUNTS_ALREADY_CONNECTED = "Email account already connected. You couldn't update email account!";
    public static final String EMAIL_ACCOUNTS_LIST_FETCH_SUCCESS = "Email accounts fetch successfully.";
    public static final String EMAIL_ACCOUNTS_FETCH_SUCCESS = "Email account fetch successfully.";
    public static final String EMAIL_ACCOUNTS_CREDENTIAL_IS_NULL = "Fetch and SMTP protocol both can not be null!";
    public static final String EMAIL_ACCOUNTS_EMAIL_SEND_FAILED = "Failed to send email to the email: ";


    /**
     * Email Account Verification Constants
     */
    public static final String ACCOUNT_VERIFICATION_EMAIL_SUBJECT = "Verify email account.";
    public static final String VERIFY_EMAIL_SUCCESS_MESSAGE = "Email account verified successfully.";
    public static final String VERIFICATION_EMAIL_CONTENT = "You are about to connect this email to PennyPerfect Email Service. Please verify your ownership by clicking the following link \n\n {{URL}}";
    public static final String EMAIL_ACCOUNTS_NOT_VERIFIED = "Email account is not verified!";

}

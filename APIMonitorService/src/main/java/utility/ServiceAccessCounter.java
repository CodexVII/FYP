/**
 * Counts the amount of times the service has been called in the API
 */
package utility;

public class ServiceAccessCounter {
	private static int searchCount;
	private static int registerCount;
	private static int deleteCount;
	private static int getCount;
	private static int changePasswordCount;
	private static int payCount;
	private static int validateCount;
	
	public static int getGetCount() {
		return getCount;
	}

	public static void setGetCount(int getCount) {
		ServiceAccessCounter.getCount = getCount;
	}

	public static int getChangePasswordCount() {
		return changePasswordCount;
	}

	public static void setChangePasswordCount(int updatePasswordCount) {
		ServiceAccessCounter.changePasswordCount = updatePasswordCount;
	}

	public static int getPayCount() {
		return payCount;
	}

	public static void setPayCount(int payCount) {
		ServiceAccessCounter.payCount = payCount;
	}

	public static int getValidateCount() {
		return validateCount;
	}

	public static void setValidateCount(int validateCount) {
		ServiceAccessCounter.validateCount = validateCount;
	}

	
	public static int getDeleteCount() {
		return deleteCount;
	}

	public static void setDeleteCount(int deleteCount) {
		ServiceAccessCounter.deleteCount = deleteCount;
	}

	public static int getRegisterCount() {
		return registerCount;
	}

	public static void setRegisterCount(int registerCount) {
		ServiceAccessCounter.registerCount = registerCount;
	}

	public static int getSearchCount() {
		return searchCount;
	}

	public static void setSearchCount(int searchCounter) {
		ServiceAccessCounter.searchCount = searchCounter;
	}
	
	/*
	 * Allows service API to update counters as they are accessed
	 */
	public static void incrementSearchCount(){
		searchCount++;
	}
	
	public static void incrementRegisterCount(){
		registerCount++;
	}
	
	public static void incrementChangePasswordCount(){
		changePasswordCount++;
	}
	
	public static void incrementDeleteCount(){
		deleteCount++;
	}
	
	public static void incrementGetCount(){
		getCount++;
	}
	
	public static void incrementPayCount(){
		payCount++;
	}
	
	public static void incrementValidateCount(){
		validateCount++;
	}
}

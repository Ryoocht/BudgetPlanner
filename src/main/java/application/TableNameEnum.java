package application;

public enum TableNameEnum {
	ELIZA_ACCOUNT("Eliza"), RYO_ACCOUNT("Ryo"), 
	RYZA_ACCOUNT("Joint Account"), JOINT_CREDIT("Credit Account");
	final private String name;
	TableNameEnum(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
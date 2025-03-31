package net.lonzak.common.unittest.examples.dtos;

import java.io.Serializable;

public class SpecialValueSetter implements Cloneable, Serializable {

	private static final long serialVersionUID = -2595640691910051059L;
	private Integer encryptionStrength;

    public Integer getEncryptionStrength() {
        return this.encryptionStrength;
    }

    /**
     * Sets the encryption strength for transferring the signature data from client to server
     * Possible values are 128, 192 or 256
     */
    public void setEncryptionStrength(Integer signatureTransferEncryptionStrength) {
        if (signatureTransferEncryptionStrength != null && signatureTransferEncryptionStrength != 128 &&
            signatureTransferEncryptionStrength != 192 &&
            signatureTransferEncryptionStrength != 256) {
            throw new IllegalArgumentException("Unsupported strength. Suppoted values are 128, 192 and 256");
        }

        this.encryptionStrength = signatureTransferEncryptionStrength;
    }

    @Override
    public SpecialValueSetter clone() throws CloneNotSupportedException {
        return (SpecialValueSetter) super.clone();
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SpecialValueSetter [");
		if (this.encryptionStrength != null) {
			builder.append("encryptionStrength=");
			builder.append(this.encryptionStrength);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.encryptionStrength == null) ? 0 : this.encryptionStrength.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpecialValueSetter other = (SpecialValueSetter) obj;
		if (this.encryptionStrength == null) {
			if (other.encryptionStrength != null)
				return false;
		} else if (!this.encryptionStrength.equals(other.encryptionStrength))
			return false;
		return true;
	}
}

package XamineRIS;

import java.awt.Image;
import java.time.LocalDate;

//TODO
//Need to fix verify
public class ImageFile {
	
	int ID;
	Image image;
	String label;
	String user;
	String path;
	
	public ImageFile(int id, Image img, String path, String lbl, String uploadUser) {
		
		this.ID = id;
		this.image = img;
		this.path = path;
		this.label = lbl;
		this.user = uploadUser;
		
	}//end Image
	
	public Boolean VerifyImage(String lbl) {//only accepts .jpg and .png THIS BROKEN***********************
		
		int startIndex = lbl.length()-4;
		Boolean isAccepted = false;
		
		//Check substring of final 4 indexes for .jpg or .png
		if (lbl.substring(startIndex, lbl.length()) == ".jpg" || lbl.substring(startIndex, lbl.length()) == ".PNG") {
			 isAccepted = true;
		}//end if
		
		return isAccepted;
		
	}//end VerifyImage

	@Override
	public String toString() {
		return "Image [image=" + image + ", label=" + label + ", user=" + user + "]";
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

}

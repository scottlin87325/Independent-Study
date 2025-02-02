package tw.brad.stest5.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class T1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String f1;
	private String f2;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getF1() {
		return f1;
	}
	public void setF1(String f1) {
		this.f1 = f1;
	}
	public String getF2() {
		return f2;
	}
	public void setF2(String f2) {
		this.f2 = f2;
	}
	//-----------------------------
	@OneToMany(mappedBy = "t1", cascade = CascadeType.ALL)
	private List<T2> t2s = new ArrayList<>();
	public List<T2> getT2s() {
		return t2s;
	}
	public void setT2s(List<T2> t2s) {
		this.t2s = t2s;
	}
	//----------------------
	public void addT2(T2 t2) {
		t2.setT1(this);
		t2s.add(t2);
	}
	
	public void removeT2(T2 t2) {
		t2.setT1(null);
		t2s.remove(t2);
	}
	
	
	
}
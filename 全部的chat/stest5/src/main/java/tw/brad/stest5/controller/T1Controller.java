package tw.brad.stest5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.brad.stest5.model.T1;
import tw.brad.stest5.model.T2;
import tw.brad.stest5.repository.T1Repository;


@RequestMapping("/t1")
@RestController
public class T1Controller {

	@Autowired
	private T1Repository t1Repository;
	
	@GetMapping("/test1")
	private void test1() {
		T1 t1 = new T1();
		t1.setF1("aaa4"); t1.setF2("bbb4");
		
		T2 t21 = new T2();
		t21.setF3("ccc9"); t21.setF4("ddd9");
		t1.addT2(t21);
		
		T2 t22 = new T2();
		t22.setF3("ccc0"); t22.setF4("ddd0");
		t1.addT2(t22);

		T2 t23 = new T2();
		t23.setF3("ccc1"); t23.setF4("ddd1");
		t1.addT2(t23);
		
		t1Repository.save(t1);
	}

	
	@GetMapping("/test2")
	public void test2() {
		Optional<T1> opt = t1Repository.findById(1L);
		T1 t1 = opt.get();
		System.out.println(t1.getF1());
		System.out.println(t1.getF2());
		List<T2> t2s = t1.getT2s();
		for (T2 t2: t2s) {
			System.out.printf("%s : %s\n", t2.getF3(), t2.getF4());
		}
		
	}
	
	@GetMapping("/test3")
	public void test3() {
		Optional<T1> opt = t1Repository.findById(1L);
		T1 t1 = opt.get();
		t1Repository.delete(t1);
		
	}
	
}
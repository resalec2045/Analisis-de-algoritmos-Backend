package co.uniquindio.proyecto.backendalgoritmos;

import org.springframework.boot.SpringApplication;

public class TestBackendalgoritmosApplication {

	public static void main(String[] args) {
		SpringApplication.from(BackendalgoritmosApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

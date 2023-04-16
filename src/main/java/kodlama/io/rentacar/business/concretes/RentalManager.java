package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.InvoiceService;
import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateInvoiceRequest;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.business.rules.RentalBusinessRules;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final ModelMapper mapper;
    private final CarService carService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final RentalBusinessRules rules;

    @Override
    public List<GetAllRentalsResponse> getAll() {
        List<Rental> rentals = repository.findAll();
        List<GetAllRentalsResponse> responses = rentals
                .stream()
                .map(rental -> mapper.map(rental, GetAllRentalsResponse.class))
                .toList();

        return responses;
    }

    @Override
    public GetRentalResponse getById(int id) {
        Rental rental = repository.findById(id).orElseThrow();
        GetRentalResponse response = mapper.map(rental, GetRentalResponse.class);

        return response;
    }

    @Override
    public GetRentalResponse returnCarFromRental(int carId) {
        rules.checkIfCarRented(carId);
        Rental rental = repository.findRentalByCarId(carId);
        rental.setEndDate(LocalDateTime.now());
        repository.save(rental);
        carService.changeState(carId, State.AVAILABLE);
        GetRentalResponse response = mapper.map(rental, GetRentalResponse.class);

        return response;
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) {
        int carId = request.getCarId();
        rules.checkIfCarAvailable(carService.getById(carId).getState());

        Rental rental = mapper.map(request, Rental.class);
        rental.setId(0);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(rental.getRentedForDays()));

        Car car = mapper.map(carService.getById(carId), Car.class);
        rental.setCar(car);
        rental.setTotalPrice(getTotalPrice(rental));

        // Payment
        CreateRentalPaymentRequest paymentRequest = new CreateRentalPaymentRequest();
        mapper.map(request.getPaymentRequest(), paymentRequest);
        paymentRequest.setPrice(getTotalPrice(rental));
        paymentService.processRentalPayment(paymentRequest);

        repository.save(rental);
        carService.changeState(rental.getCar().getId(), State.RENTED);
        CreateRentalResponse response = mapper.map(rental, CreateRentalResponse.class);

        // Invoice
        CreateInvoiceRequest invoiceRequest = new CreateInvoiceRequest();
        createInvoiceRequest(invoiceRequest, car, rental, paymentRequest);
        invoiceService.add(invoiceRequest);

        return response;
    }

    @Override
    public UpdateRentalResponse update(int id, UpdateRentalRequest request) {
        rules.checkIfRentalExist(id);
        Rental rental = mapper.map(request, Rental.class);
        Car car = mapper.map(carService.getById(request.getCarId()), Car.class);

        carService.changeState(rental.getCar().getId(), State.AVAILABLE);

        rental.setId(id);
        rental.setCar(car);
        rental.setTotalPrice(getTotalPrice(rental));
        carService.changeState(car.getId(), State.RENTED);

        repository.save(rental);
        UpdateRentalResponse response = mapper.map(rental, UpdateRentalResponse.class);

        return response;
    }

    @Override
    public void delete(int id) {
        rules.checkIfRentalExist(id);
        int carId = repository.findById(id).get().getCar().getId();
        carService.changeState(carId, State.AVAILABLE);
//        invoiceService.deleteByRentalId(id);

        repository.deleteById(id);
    }


    private double getTotalPrice(Rental rental) {
        return rental.getCar().getDailyPrice() * rental.getRentedForDays();
    }


    private void createInvoiceRequest(CreateInvoiceRequest invoiceRequest, Car car, Rental rental, CreateRentalPaymentRequest paymentRequest) {
        invoiceRequest.setRentedAt(rental.getStartDate());
        invoiceRequest.setModelName(car.getModel().getName());
        invoiceRequest.setBrandName(car.getModel().getBrand().getName());
        invoiceRequest.setDailyPrice(car.getDailyPrice());
        invoiceRequest.setRentedForDays(rental.getRentedForDays());
        invoiceRequest.setCardHolder(paymentRequest.getCardHolder());
        invoiceRequest.setPlate(car.getPlate());
        invoiceRequest.setModelYear(car.getModelYear());
    }
}

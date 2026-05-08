import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

type Role = 'ADMIN' | 'USER';
type TaxiStatus = 'AVAILABLE' | 'BUSY' | 'MAINTENANCE';
type TripStatus = 'REQUESTED' | 'ASSIGNED' | 'COMPLETED' | 'CANCELLED';

interface User {
  id: number;
  name: string;
  email: string;
  role: Role;
}

interface AuthResponse {
  token: string;
  user: User;
}

interface Taxi {
  id: number;
  plate: string;
  driverName: string;
  vehicleModel: string;
  capacity: number;
  baseFare: number;
  status: TaxiStatus;
}

interface Trip {
  id: number;
  passengerName: string;
  pickupAddress: string;
  destinationAddress: string;
  taxiPlate: string;
  fare: number;
  status: TripStatus;
  requestedBy: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private readonly authUrl = 'http://localhost:8081/api/auth';
  private readonly taxiUrl = 'http://localhost:8082/api/taxis';
  private readonly tripUrl = 'http://localhost:8083/api/trips';

  token = localStorage.getItem('token') ?? '';
  user: User | null = this.readUser();
  activeTab: 'taxis' | 'trips' = 'taxis';
  loading = false;
  message = '';
  error = '';
  search = '';

  loginForm = {
    email: 'admin@taxis.com',
    password: 'admin123'
  };

  registerForm = {
    name: '',
    email: '',
    password: '',
    role: 'USER' as Role
  };

  taxiStatuses: TaxiStatus[] = ['AVAILABLE', 'BUSY', 'MAINTENANCE'];
  tripStatuses: TripStatus[] = ['REQUESTED', 'ASSIGNED', 'COMPLETED', 'CANCELLED'];

  taxis: Taxi[] = [];
  trips: Trip[] = [];
  editingTaxiId: number | null = null;
  editingTripId: number | null = null;

  taxiForm = this.emptyTaxiForm();
  tripForm = this.emptyTripForm();

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    if (this.token) {
      this.loadData();
    }
  }

  get isAdmin(): boolean {
    return this.user?.role === 'ADMIN';
  }

  get availableTaxis(): number {
    return this.taxis.filter((taxi) => taxi.status === 'AVAILABLE').length;
  }

  login(): void {
    this.postAuth('/login', this.loginForm);
  }

  register(): void {
    this.postAuth('/register', this.registerForm);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.token = '';
    this.user = null;
    this.taxis = [];
    this.trips = [];
  }

  loadData(): void {
    this.loadTaxis();
    this.loadTrips();
  }

  loadTaxis(): void {
    this.http.get<Taxi[]>(this.withSearch(this.taxiUrl), this.authHeaders()).subscribe({
      next: (taxis) => this.taxis = taxis,
      error: (err) => this.handleError(err)
    });
  }

  loadTrips(): void {
    this.http.get<Trip[]>(this.withSearch(this.tripUrl), this.authHeaders()).subscribe({
      next: (trips) => this.trips = trips,
      error: (err) => this.handleError(err)
    });
  }

  saveTaxi(): void {
    const payload = {
      ...this.taxiForm,
      capacity: Number(this.taxiForm.capacity),
      baseFare: Number(this.taxiForm.baseFare)
    };

    const request = this.editingTaxiId
      ? this.http.put<Taxi>(`${this.taxiUrl}/${this.editingTaxiId}`, payload, this.authHeaders())
      : this.http.post<Taxi>(this.taxiUrl, payload, this.authHeaders());

    request.subscribe({
      next: () => {
        this.success(this.editingTaxiId ? 'Taxi actualizado' : 'Taxi creado');
        this.cancelTaxiEdit();
        this.loadTaxis();
      },
      error: (err) => this.handleError(err)
    });
  }

  editTaxi(taxi: Taxi): void {
    this.editingTaxiId = taxi.id;
    this.taxiForm = {
      plate: taxi.plate,
      driverName: taxi.driverName,
      vehicleModel: taxi.vehicleModel,
      capacity: taxi.capacity,
      baseFare: taxi.baseFare,
      status: taxi.status
    };
  }

  deleteTaxi(id: number): void {
    this.http.delete(`${this.taxiUrl}/${id}`, this.authHeaders()).subscribe({
      next: () => {
        this.success('Taxi eliminado');
        this.loadTaxis();
      },
      error: (err) => this.handleError(err)
    });
  }

  cancelTaxiEdit(): void {
    this.editingTaxiId = null;
    this.taxiForm = this.emptyTaxiForm();
  }

  saveTrip(): void {
    const payload = {
      ...this.tripForm,
      fare: Number(this.tripForm.fare)
    };

    const request = this.editingTripId
      ? this.http.put<Trip>(`${this.tripUrl}/${this.editingTripId}`, payload, this.authHeaders())
      : this.http.post<Trip>(this.tripUrl, payload, this.authHeaders());

    request.subscribe({
      next: () => {
        this.success(this.editingTripId ? 'Carrera actualizada' : 'Carrera creada');
        this.cancelTripEdit();
        this.loadTrips();
      },
      error: (err) => this.handleError(err)
    });
  }

  editTrip(trip: Trip): void {
    this.editingTripId = trip.id;
    this.tripForm = {
      passengerName: trip.passengerName,
      pickupAddress: trip.pickupAddress,
      destinationAddress: trip.destinationAddress,
      taxiPlate: trip.taxiPlate,
      fare: trip.fare,
      status: trip.status
    };
  }

  deleteTrip(id: number): void {
    this.http.delete(`${this.tripUrl}/${id}`, this.authHeaders()).subscribe({
      next: () => {
        this.success('Carrera eliminada');
        this.loadTrips();
      },
      error: (err) => this.handleError(err)
    });
  }

  cancelTripEdit(): void {
    this.editingTripId = null;
    this.tripForm = this.emptyTripForm();
  }

  statusText(status: TaxiStatus | TripStatus): string {
    const labels: Record<string, string> = {
      AVAILABLE: 'Disponible',
      BUSY: 'Ocupado',
      MAINTENANCE: 'Mantenimiento',
      REQUESTED: 'Solicitada',
      ASSIGNED: 'Asignada',
      COMPLETED: 'Completada',
      CANCELLED: 'Cancelada'
    };
    return labels[status];
  }

  statusClass(status: TaxiStatus | TripStatus): string {
    return status.toLowerCase();
  }

  money(value: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0
    }).format(value);
  }

  private postAuth(path: string, body: object): void {
    this.loading = true;
    this.error = '';
    this.message = '';

    this.http.post<AuthResponse>(`${this.authUrl}${path}`, body).subscribe({
      next: (response) => {
        this.loading = false;
        this.token = response.token;
        this.user = response.user;
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
        this.success(`Sesion iniciada como ${response.user.role}`);
        this.loadData();
      },
      error: (err) => {
        this.loading = false;
        this.handleError(err);
      }
    });
  }

  private authHeaders(): { headers: HttpHeaders } {
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.token}`
      })
    };
  }

  private withSearch(url: string): string {
    return this.search.trim()
      ? `${url}?search=${encodeURIComponent(this.search.trim())}`
      : url;
  }

  private success(message: string): void {
    this.message = message;
    this.error = '';
  }

  private handleError(err: unknown): void {
    const response = err as { error?: { message?: string }; message?: string };
    this.error = response.error?.message ?? response.message ?? 'No se pudo completar la operacion';
    this.message = '';
  }

  private readUser(): User | null {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) as User : null;
  }

  private emptyTaxiForm() {
    return {
      plate: '',
      driverName: '',
      vehicleModel: '',
      capacity: 4,
      baseFare: 6500,
      status: 'AVAILABLE' as TaxiStatus
    };
  }

  private emptyTripForm() {
    return {
      passengerName: '',
      pickupAddress: '',
      destinationAddress: '',
      taxiPlate: '',
      fare: 15000,
      status: 'REQUESTED' as TripStatus
    };
  }
}

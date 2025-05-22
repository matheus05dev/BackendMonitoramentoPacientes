import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuartosListaComponent } from './quartos-lista.component';

describe('QuartosListaComponent', () => {
  let component: QuartosListaComponent;
  let fixture: ComponentFixture<QuartosListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuartosListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuartosListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

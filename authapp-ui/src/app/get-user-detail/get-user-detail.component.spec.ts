import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetUserDetailComponent } from './get-user-detail.component';

describe('GetUserDetailComponent', () => {
  let component: GetUserDetailComponent;
  let fixture: ComponentFixture<GetUserDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetUserDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetUserDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

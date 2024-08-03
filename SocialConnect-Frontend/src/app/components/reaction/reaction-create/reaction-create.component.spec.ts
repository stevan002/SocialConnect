import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactionCreateComponent } from './reaction-create.component';

describe('ReactionCreateComponent', () => {
  let component: ReactionCreateComponent;
  let fixture: ComponentFixture<ReactionCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReactionCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReactionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

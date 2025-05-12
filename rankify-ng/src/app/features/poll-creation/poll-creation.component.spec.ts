import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { PollCreationComponent } from './poll-creation.component';
import { PollService } from '../../core/services/poll.service';
import { of } from 'rxjs';

describe('PollCreationComponent', () => {
  let component: PollCreationComponent;
  let fixture: ComponentFixture<PollCreationComponent>;
  let pollServiceSpy: jasmine.SpyObj<PollService>;

  beforeEach(async () => {
    pollServiceSpy = jasmine.createSpyObj('PollService', ['createPoll']);
    pollServiceSpy.createPoll.and.returnValue(of({
      id: '1', 
      name: 'Test Poll', 
      isOpen: false, 
      options: [
        { id: '1', name: 'Option 1' }, 
        { id: '2', name: 'Option 2' }
      ],
      createdAt: new Date()
    }));

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule, PollCreationComponent],
      providers: [
        { provide: PollService, useValue: pollServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PollCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should correctly handle option deletion when empty options exist', () => {
    // SETUP: Initial state with a name and two filled options
    component.name.setValue('Test-Poll');
    const optionsArray = component.options;
    optionsArray.controls[0].setValue('Option 1');
    optionsArray.controls[1].setValue('Option 2');
    
    // Verify form is valid with two filled options
    expect(component.formValid).toBeTrue();
    
    // STEP 1: Add a third option (which is empty by default)
    component.addOption();
    
    // Verify we now have three options with the last one empty
    expect(optionsArray.length).toBe(3);
    expect(optionsArray.controls[0].value).toBe('Option 1');
    expect(optionsArray.controls[1].value).toBe('Option 2');
    expect(optionsArray.controls[2].value).toBe('');
    
    // Form should be invalid because we have an empty option
    expect(component.formValid).toBeFalse();
    
    // STEP 2: Remove the first filled option
    const indexToRemove = 0; // This is "Option 1"
    component.removeOption(indexToRemove);
    
    // We should still have "Option 2" and an empty option
    expect(optionsArray.length).toBe(2);
    
    // Check if we still have the second option
    const hasOption2 = optionsArray.controls.some(control => control.value === 'Option 2');
    expect(hasOption2).toBeTrue();
    
    // We should still have an empty option
    const hasEmptyOption = optionsArray.controls.some(control => control.value === '');
    expect(hasEmptyOption).toBeTrue();
    
    // Form should be invalid because we have an empty option
    expect(component.formValid).toBeFalse();
    
    // Fill the empty option
    const emptyOptionIndex = 1;
    optionsArray.controls[emptyOptionIndex].setValue('New Option');
    
    // Now the form should be valid again
    expect(component.formValid).toBeTrue();
  });
});
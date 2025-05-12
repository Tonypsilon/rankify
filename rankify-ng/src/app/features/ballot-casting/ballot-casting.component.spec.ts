import { BallotCastingComponent } from './ballot-casting.component';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { PollOption } from '../../core/models/poll.model';

describe('BallotCastingComponent', () => {
  let component: BallotCastingComponent;
  const dummyOption1: PollOption = { id: '1', name: 'Option 1' };
  const dummyOption2: PollOption = { id: '2', name: 'Option 2' };

  beforeEach(() => {
    // Provide minimal constructor dependencies as any
    component = new BallotCastingComponent({} as any, {} as any, {} as any, {} as any);
  });

  it('should automatically add a new empty rank group when all groups have at least one entry after drop', () => {
    // Setup: one existing group with one option, unranked has another
    component.rankGroups = [[dummyOption1]];
    component.unrankedOptions = [dummyOption2];

    // Simulate drop from unranked (previousContainer) to first group (container)
    const event = {
      previousContainer: { data: component.unrankedOptions },
      container: { data: component.rankGroups[0] },
      previousIndex: 0,
      currentIndex: 1
    } as unknown as CdkDragDrop<PollOption[]>;

    component.drop(event);

    // After drop: rankGroups should have two groups
    expect(component.rankGroups.length).toBe(2);
    // First group should contain both options
    expect(component.rankGroups[0]).toEqual([dummyOption1, dummyOption2]);
    // Second group should be empty
    expect(component.rankGroups[1]).toEqual([]);
  });

  it('should not add a new group on reorder within same group', () => {
    // Setup: one group with two options
    component.rankGroups = [[dummyOption1, dummyOption2]];

    // Simulate drop within the same container
    const event = {
      previousContainer: { data: component.rankGroups[0] },
      container: { data: component.rankGroups[0] },
      previousIndex: 0,
      currentIndex: 1
    } as unknown as CdkDragDrop<PollOption[]>;

    component.drop(event);

    // Should remain one group and order swapped
    expect(component.rankGroups.length).toBe(1);
    expect(component.rankGroups[0]).toEqual([dummyOption2, dummyOption1]);
  });

  it('should allow direct moves between non-empty levels and preserve empty levels without adding new ones', () => {
    component.rankGroups = [[dummyOption1], [dummyOption2]];
    component.unrankedOptions = [];

    // Simulate drop from level 1 (index 0) to level 2 (index 1)
    const event = {
      previousContainer: { data: component.rankGroups[0] },
      container: { data: component.rankGroups[1] },
      previousIndex: 0,
      currentIndex: 2
    } as unknown as CdkDragDrop<PollOption[]>;

    component.drop(event);

    // Expect group 0 to be empty, group 1 to contain both options
    expect(component.rankGroups.length).toBe(2);
    expect(component.rankGroups[0]).toEqual([]);
    expect(component.rankGroups[1]).toEqual([dummyOption2, dummyOption1]);
  });

  it('should preserve existing empty levels when moving an option into them', () => {
    component.rankGroups = [[dummyOption1], [dummyOption2], []];
    component.unrankedOptions = [];

    // Simulate drop from level 1 (index 0) to empty level (index 2)
    const event = {
      previousContainer: { data: component.rankGroups[0] },
      container: { data: component.rankGroups[2] },
      previousIndex: 0,
      currentIndex: 0
    } as unknown as CdkDragDrop<PollOption[]>;

    component.drop(event);

    // Expect three groups still: first now empty, second unchanged, third has dummyOption1
    expect(component.rankGroups.length).toBe(3);
    expect(component.rankGroups[0]).toEqual([]);
    expect(component.rankGroups[1]).toEqual([dummyOption2]);
    expect(component.rankGroups[2]).toEqual([dummyOption1]);
  });
});